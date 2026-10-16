const { GraphQLObjectType, GraphQLString, GraphQLSchema, GraphQLList, GraphQLNonNull } = require('graphql');
const { Persona, Profesion } = require('./database');

// Definición del tipo Persona en GraphQL
const PersonaType = new GraphQLObjectType({
  name: 'Persona',
  fields: {
    id: { type: GraphQLString },
    nombres: { type: GraphQLString },
    apellidos: { type: GraphQLString },
    documento_identidad: { type: GraphQLString },
    sexo: { type: GraphQLString },
    fecha_nacimiento: { type: GraphQLString },
    celular: { type: GraphQLString }
  }
});

//esquema para Profesion:
const ProfesionType = new GraphQLObjectType({
  name: 'Profesion',

  fields: {
    id: { type: GraphQLString },
    nombre: { type: GraphQLString },
    nivel: { type: GraphQLString }
  }
});

// Definición de las consultas (queries)
const RootQuery = new GraphQLObjectType({
  name: 'RootQueryType',
  fields: {
    personas: {
      type: new GraphQLList(PersonaType),
      resolve(parent, args) {
        // Obtener todas las personas
        return Persona.findAll();
      }
    },
    persona: {
      type: PersonaType,
      args: { id: { type: GraphQLString } },
      resolve(parent, args) {
        // Buscar una persona por ID
        return Persona.findByPk(args.id);
      }
    },
    profesiones: {
      type: new GraphQLList(ProfesionType),

      resolve(parent, args) {
        return Profesion.findAll();
    }
    }
  }
});

// Definición de las mutaciones
const Mutation = new GraphQLObjectType({
  name: 'Mutation',
  fields: {
    addPersona: {
      type: PersonaType,
      args: {
        nombres: { type: new GraphQLNonNull(GraphQLString) },
        apellidos: { type: GraphQLString },
        documento_identidad: { type: new GraphQLNonNull(GraphQLString) },
        sexo: { type: GraphQLString },
        fecha_nacimiento: { type: GraphQLString },
        celular: { type: GraphQLString }
      },
      resolve(parent, args) {
        // Crear una nueva persona
        return Persona.create({
          nombres: args.nombres,
          apellidos: args.apellidos,
          documento_identidad: args.documento_identidad,
          sexo: args.sexo,
          fecha_nacimiento: args.fecha_nacimiento,
          celular: args.celular
        });
      }
    },
    updatePersona: {
      type: PersonaType,
      args: {
        id: { type: new GraphQLNonNull(GraphQLString) },
        nombres: { type: GraphQLString },
        apellidos: { type: GraphQLString },
        documento_identidad: { type: GraphQLString },
        sexo: { type: GraphQLString },
        fecha_nacimiento: { type: GraphQLString },
        celular: { type: GraphQLString }
      },
      resolve(parent, args) {
        // Actualizar una persona existente
        return Persona.findByPk(args.id)
          .then(persona => {
            if (!persona) {
              throw new Error('Persona no encontrada');
            }
            return persona.update({
              nombres: args.nombres !== undefined ? args.nombres : persona.nombres,
              apellidos: args.apellidos !== undefined ? args.apellidos : persona.apellidos,
              documento_identidad: args.documento_identidad !== undefined ? args.documento_identidad : persona.documento_identidad,
              sexo: args.sexo !== undefined ? args.sexo : persona.sexo,
              fecha_nacimiento: args.fecha_nacimiento !== undefined ? args.fecha_nacimiento : persona.fecha_nacimiento,
              celular: args.celular !== undefined ? args.celular : persona.celular
            });
          });
      }
    },
    addProfesion: {
      type: ProfesionType,
      args: {
        nombre: {type: new GraphQLNonNull(GraphQLString)},
        nivel: {type: new GraphQLNonNull(GraphQLString)}
      },
      resolve(parent, args) {
        return Profesion.create({
          nombre: args.nombre,
          nivel: args.nivel
        });

      }
    }
  }
});

// Exporta el esquema GraphQL
module.exports = new GraphQLSchema({
  query: RootQuery,
  mutation: Mutation
});
