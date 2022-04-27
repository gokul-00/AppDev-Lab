const express = require('express');
const app = express();
const cors = require('cors');
require('dotenv').config({ path: './.env' });
app.use(cors());
app.use(express.json());
const database = require('./db');

// require('./database/init.js');
// app.post('/login', (req, res) => {
//   console.log(req.body);
//   const { rollNo, password } = req.body;
//   if (!rollNo || !password) {
//     return res.status(400).json({
//       message: 'Please enter rollNo and password',
//     });
//   }
//   const user = await database.Users.findOne({
//     where: {
//       rollNo,
//       password,
//     },
//   });
//   console.log(user);
//   return res.status(200).json({ user });
// });
// app.delete('/del', async (req, res) => {
//   try {
//     const { rollNo, eventName } = req.body;
//     const user = await database.Users.findOne({
//       where: { rollNo },
//     });
//     const eventList = JSON.parse(user.events);
//     const index = eventList.indexOf(eventName);
//     if (index > -1) {
//       eventList.splice(index, 1);
//     }
//     await database.Users.update(
//       { events: JSON.stringify(eventList) },
//       { where: { rollNo } }
//     );

//     return res.status(200).json({ message: 'Success' });
//   } catch (error) {
//     console.log(error.message);
//     return res.status(500).json({ message: 'Server error' });
//   }
// });
app.get('/all', async (req, res) => {
	try {
		const details = await database.Users.findAll({});
		return res.status(200).json({ details });
	} catch (error) {
		console.log(error.message);
		return res.status(500).json({ message: 'Server error' });
	}
});
app.get('/events', async (req, res) => {
	try {
		const name = req.query.eventName;
		const details = await database.Events.findOne({
			name,
		});
		return res.status(200).json({ details });
	} catch (error) {
		console.log(error.message);
		return res.status(500).json({ message: 'Server error' });
	}
});
app.delete('/stud_del', async (req, res) => {
	try {
		const { rollNo } = req.query;
		await database.Users.destroy({
			where: {
				rollNo,
			},
		});
		return res.status(200).json({ message: 'Success' });
	} catch (error) {
		console.log(error.message);

		return res.status(500).json({ message: 'Server error' });
	}
});
app.post('/add', async (req, res) => {
	try {
		const { rollNo, name, course, events, edit } = req.body;
		if (!rollNo || !name || !course) {
			return res.status(400).json({
				message: 'Please enter all the details',
			});
		}
		const userExist = await database.Users.findOne({ where: { rollNo } });
		if (userExist) {
			if (!edit) {
				return res.status(400).json({ message: 'User already exists' });
			}
			const user = await database.Users.update(
				{ event: events, course, name },
				{ where: { rollNo } }
			);

			return res.status(200).json({ message: 'Success' });
		}

		const user = await database.Users.create({
			rollNo,
			name,
			event: events,
			course,
		});
		return res.status(200).json({ message: 'Success' });
	} catch (error) {
		console.log(error.message);
		return res.status(500).json({ message: 'Server error' });
	}
});
app.listen(3001, () => {
	console.log('Server listening on port 3001');
});
