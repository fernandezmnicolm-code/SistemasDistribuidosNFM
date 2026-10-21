const { Sequelize } = require('sequelize');

const sequelize = new Sequelize('bd_agenda_api', 'root', '', {
  host: 'localhost',
  dialect: 'mysql'
});


const Persona = sequelize.define('Persona', {
  id: {
    type: Sequelize.BIGINT,
    allowNull: false,
    primaryKey: true,
    autoIncrement: true,
    unsigned: true
  },
  nombres: {
    type: Sequelize.STRING(50),
    allowNull: false
  },
  apellidos: {
    type: Sequelize.STRING(50),
    allowNull: true
  },
  documento_identidad: {
    type: Sequelize.STRING(20),
    allowNull: false
  },
  sexo: {
    type: Sequelize.STRING(20),
    allowNull: true
  },
  fecha_nacimiento: {
    type: Sequelize.DATEONLY,
    allowNull: true
  },
  celular: {
    type: Sequelize.STRING(20),
    allowNull: true
  }
}, {
  tableName: 'personas',
  timestamps: true,
  createdAt: 'created_at',
  updatedAt: 'updated_at'
});

//tabla para profesion:
const Profesion = sequelize.define('Profesion', {
  id: {
    type: Sequelize.BIGINT,
    allowNull: false,
    primaryKey: true,
    autoIncrement: true,
    unsigned: true
  },

  nombre: {
    type: Sequelize.STRING(100),
    allowNull: false
  },

  nivel: {
    type: Sequelize.STRING(50),
    allowNull: true
  }

}, {
  tableName: 'profesiones',
  timestamps: false
});

// Nota: no se usa sequelize.sync() porque la tabla 'personas'
// ya existe con datos. sync({ alter: true }) podría modificarla.

module.exports = { sequelize, Persona, Profesion
 };
