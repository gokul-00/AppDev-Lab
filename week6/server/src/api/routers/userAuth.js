const authRouter = require('express').Router()

const { createJWTtoken } = require('../../middlewares/jwt')

const fs = require('fs')
const convert = require('xml-js')

authRouter.post('/login', async (req, res) => {
    try {
        const { username, password } = req.body
        console.log(username, password)
        // cred validations
        if (!username || !password) {
            return res.status(400).json({
                message: 'Fill all the fields'
            })
        }

        let xmldata = fs.readFileSync('./db.xml', 'utf8')

        const result = convert.xml2js(xmldata, {
            compact: true,
            spaces: 2
        })
        console.log(result)
        for (let i = 0; i < result.data.user.length; i++) {
            const element = result.data.user[i]
            if (element.username._text == username) {
                if (element.password._text == password) {
                    const token = await createJWTtoken(element.username._text)
                    // res.cookie('token', token)
                    return res.status(200).json({
                        message: 'Success',
                        token: token
                    })
                }
                return res.status(401).json({
                    message: 'Incorrect username or password'
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

authRouter.post('/register', async (req, res) => {
    try {
        const { username, roll, name, email, cpassword, password, branch, phone, address } =
            req.body

        // cred validations
        if (
            !username ||
            !roll ||
            !password ||
            !name ||
            !email ||
            !cpassword ||
            !branch ||
            !phone ||
            !address
        ) {
            return res.status(400).json({
                message: 'Fill all the fields'
            })
        }

        if (password !== cpassword) {
            return res.status(400).json({
                message: 'Password does not match'
            })
        }

        let xmldata = fs.readFileSync('./db.xml', 'utf8')

        const result = convert.xml2js(xmldata, {
            compact: true,
            spaces: 2
        })

        for (let i = 0; i < result.data.user.length; i++) {
            const element = result.data.user[i]

            if (element.username._text == username) {
                return res.status(400).json({
                    message: 'Account already exists'
                })
            }
        }

        result.data.user.push({
            username: { _text: username },
            roll: { _text: roll },
            name: { _text: name },
            email: { _text: email },
            password: { _text: password },
            branch: { _text: branch },
            address: { _text: address },
            phone: { _text: phone }
        })

        const newData = convert.js2xml(result, { compact: true, spaces: 2 })

        fs.writeFileSync('./db.xml', newData, function (err) {
            if (err) throw err
        })

        return res.status(200).json({
            message: 'Success'
        })
    } catch (err) {
        console.log(err.message)
        return res.status(500).json({
            message: 'Server error'
        })
    }
})

module.exports = authRouter
