require('dotenv').config({ path: './src/env/.env' })
const express = require('express')
const app = express()
const cors = require('cors')
const fs = require('fs')
const path = require('path')
const cookieParser = require('cookie-parser')

// var xml = builder.create('data').ele('user').end({ pretty: true });

// if (!fs.existsSync('./db.xml')) {
// 	fs.writeFileSync('./db.xml', xml, function (err) {
// 		if (err) throw err;
// 	});
// }
app.use(cookieParser())
app.use(cors())
app.use(express.urlencoded({ extended: false }))
app.use(express.json())

const apiRouter = require('./src/api/api')

app.use('/', apiRouter)

app.listen(3000, () => {
    console.log('Server is running on port 3000')
})
