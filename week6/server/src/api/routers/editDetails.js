const editRouter = require('express').Router()
const sendMail = require('./utlities/mailer')

const { createOTPtoken, verifyUserOTP } = require('../../middlewares/jwt')
var randomNumber = require('random-number-csprng')

const fs = require('fs')
const convert = require('xml-js')

editRouter.get('/sendOtp', async (req, res) => {
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
            // console.log(element.rollNo._text);

            if (element.username._text == username) {
                const email = element.email._text
                const otpNumber = await randomNumber(100000, 999999)

                result.data.user[i].otp = otpNumber

                data = convert.js2xml(result, { compact: true, spaces: 2 })

                fs.writeFileSync('./db.xml', data, function (err) {
                    if (err) throw err
                })

                //send otp in mail
                sendMail(otpNumber, email)

                console.log(otpNumber)
                return res.status(200).json({
                    message: 'success',
                    otp: 'otp'
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

editRouter.post('/verifyOtp', async (req, res) => {
    try {
        const username = req.jwt_payload.username

        // cred validations
        if (!username) {
            return res.status(401).json({
                message: 'Unauthorized'
            })
        }

        if (!req.body.otp) {
            return res.status(400).json({
                message: 'Fill all the fields'
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
                if (!element.otp) {
                    return res.status(401).json({
                        message: 'OTP not sent'
                    })
                }
                if (element.otp._text == req.body.otp) {
                    const token = await createOTPtoken(element.username._text)
                    // res.cookie('otptoken', token)
                    return res.status(200).json({
                        message: 'success',
                        otptoken: token
                    })
                }
                return res.status(401).json({
                    message: 'Incorrect OTP'
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

editRouter.post('/editDetails', verifyUserOTP, async (req, res) => {
    try {
        const username = req.jwt_payload.username

        // cred validations
        if (!username) {
            return res.status(401).json({
                message: 'Unauthorized'
            })
        }

        const { name, email, roll, branch, phone, address } = req.body

        // cred validations
        if (!name || !email || !roll || !phone || !branch || !address) {
            return res.status(400).json({
                message: 'Fill all the fields'
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
                result.data.user[i].name = name
                result.data.user[i].email = email

                result.data.user[i].branch = branch
                result.data.user[i].phone = phone
                result.data.user[i].address = address
                result.data.user[i].roll = roll
                data = convert.js2xml(result, { compact: true, spaces: 2 })

                fs.writeFileSync('./db.xml', data, function (err) {
                    if (err) throw err
                })

                return res.status(200).json({
                    message: 'success'
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

module.exports = editRouter
