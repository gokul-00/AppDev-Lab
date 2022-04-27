const database = require('./db');
const initialization = async () => {
  try {
    await database.testCon();
    await database.Events.create({
      name: 'Blind Coding',
      price: 100,
    });
    await database.Events.create({
      name: 'Overnight Coding',
      price: 100,
    });
    await database.Events.create({
      name: 'Games',
      price: 100,
    });
    await database.Events.create({
      name: 'Online Treasure Hunt',
      price: 100,
    });
    await database.Events.create({
      name: 'Puzzles',
      price: 100,
    });
  } catch (error) {
    console.log(error.message);
  }
};
initialization();
