require('dotenv').config({ path: './.env' });

const { Sequelize, DataTypes } = require('sequelize');

const sequelize = new Sequelize(
	process.env.DB_NAME,
	process.env.DB_USER,
	process.env.DB_PASS,
	{
		host: 'localhost',
		dialect: 'mysql',
		logging: false,
	}
);
const testCon = async () => {
	try {
		await sequelize.authenticate();
		console.log('Connection has been established successfully.');
	} catch (error) {
		console.error('Unable to connect to the database:', error);
	}
};

const Events = sequelize.define(
	'events',
	{
		name: {
			type: DataTypes.STRING,
			allowNull: false,
			primaryKey: true,
		},
		price: {
			type: DataTypes.INTEGER,
			allowNull: false,
		},
	},
	{
		timestamps: false,
	}
);

const Users = sequelize.define(
	'users',
	{
		name: {
			type: DataTypes.STRING,
			allowNull: false,
		},
		rollNo: {
			type: DataTypes.STRING,
			allowNull: false,
			primaryKey: true,
		},
		course: {
			type: DataTypes.STRING,
		},
		event: {
			type: DataTypes.STRING,
		},
	},
	{
		timestamps: false,
	}
);
module.exports = { Users, Events, testCon };
