const dashboardRouter = require('express').Router()

require('dotenv').config({ path: '../../env/.env' })
const fs = require('fs')
const convert = require('xml-js')

dashboardRouter.get('/', async (req, res) => {
    try {
        const username = req.jwt_payload.username

        // cred validations
        if (!username) {
            return res.status(401).json({
                message: 'Unauthorized'
            })
        }

        let xmldata = fs.readFileSync('./db.xml', 'utf8')

        const result = convert.xml2js(xmldata, {
            compact: true,
            spaces: 2
        })

        for (let i = 0; i < result.data.user.length; i++) {
            const element = result.data.user[i]
            console.log(element.username._text)
            if (element.username._text == username) {
                return res.status(200).json({
                    username: element.username._text,
                    roll: element.roll._text,
                    name: element.name._text,
                    email: element.email._text,
                    phone: element.phone._text,
                    address: element.address._text,
                    branch: element.branch._text
                })
            }
        }

        return res.status(404).json({
            message: 'Account does not exist. Please sign up'
        })
    } catch (err) {
        console.log(err.message)
        return res.status(500).json({
            message: 'Server error'
        })
    }
})

module.exports = dashboardRouter
