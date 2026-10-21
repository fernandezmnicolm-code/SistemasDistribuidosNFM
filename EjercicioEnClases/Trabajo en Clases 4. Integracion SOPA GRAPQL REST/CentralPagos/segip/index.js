const express = require('express');
const cors = require('cors');
const { createHandler } = require('graphql-http/lib/use/express');
const { buildSchema } = require('graphql');

const app = express();

app.use(cors());

// Datos de prueba de SEGIP
const personas = [
    {
        CI: "147255465",
        Nombres: "Juan",
        PrimerApellido: "Perez",
        SegundoApellido: "Garcia"
    },
    {
        CI: "12345678",
        Nombres: "Maria",
        PrimerApellido: "Lopez",
        SegundoApellido: "Mendoza"
    },
    {
        CI: "87654321",
        Nombres: "Carlos",
        PrimerApellido: "Gomez",
        SegundoApellido: "Rojas"
    }
];

// Esquema GraphQL
const schema = buildSchema(`
    type Persona {
        CI: String
        Nombres: String
        PrimerApellido: String
        SegundoApellido: String
    }

    type Query {
        personas: [Persona]
        persona(CI: String!): Persona
    }
`);

// Funciones para consultar personas
const root = {
    personas: () => personas,

    persona: ({ CI }) => {
        return personas.find(p => p.CI === CI);
    }
};

// Servicio GraphQL
app.all('/graphql', createHandler({
    schema: schema,
    rootValue: root
}));

// Iniciar servidor
app.listen(4000, () => {
    console.log('SEGIP GraphQL funcionando en http://localhost:4000/graphql');
});