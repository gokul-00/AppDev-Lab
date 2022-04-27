const usernameElement = document.getElementById('username')
const emailElement = document.getElementById('email')
const nameElement = document.getElementById('name')
const rollElement = document.getElementById('roll')
const phoneElement = document.getElementById('phone')
const branchElement = document.getElementById('branch')
const addressElement = document.getElementById('address')

// const getDetails = document.getElementById('getdetails')

window.onload = async function () {
    // alert('Bye')
    try {
        console.log(localStorage.getItem('token'))
        const response = await fetch('http://localhost:3000/user', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                token: localStorage.getItem('token')
            }
        })
        const data = await response.json()
        console.log(data)
        if (response.ok) {
            usernameElement.innerHTML = data.username
            emailElement.innerHTML = data.email
            nameElement.innerHTML = data.name
            rollElement.innerHTML = data.roll
            phoneElement.innerHTML = data.phone
            branchElement.innerHTML = data.branch
            addressElement.innerHTML = data.address
        } else {
            throw new Error(data.message)
        }
    } catch (e) {
        console.log(e.message)
        alert(e.message)
    }
}

const sendOTPButton = document.getElementById('sendotp')
console.log(sendOTPButton)

sendOTPButton.onclick = async function () {
    try {
        const response = await fetch('http://localhost:3000/edit/sendOtp', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                token: localStorage.getItem('token')
            }
        })
        const data = await response.json()
        if (response.ok) {
            // alert('Hi')
            console.log(data.message)
            // alert('hi')
        } else {
            throw new Error(data.message)
        }
    } catch (e) {
        alert(e.message)
    }
}

const proceedButton = document.getElementById('proceed')
const otpElement = document.getElementById('otp')

const usernameFormElement = document.getElementById('usernameForm')
const emailFormElement = document.getElementById('emailForm')
const nameFormElement = document.getElementById('nameForm')
const rollFormElement = document.getElementById('rollForm')
const phoneFormElement = document.getElementById('phoneForm')
const branchFormElement = document.getElementById('branchForm')
const addressFormElement = document.getElementById('addressForm')

usernameFormElement.disabled = true

proceedButton.addEventListener('click', async (e) => {
    e.preventDefault()
    const otp = otpElement.value
    try {
        const response = await fetch('http://localhost:3000/edit/verifyOtp', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                token: localStorage.getItem('token')
            },
            body: JSON.stringify({
                otp
            })
        })
        const data = await response.json()
        if (response.ok) {
            localStorage.setItem('otptoken', data.otptoken)
            const userResponse = await fetch('http://localhost:3000/user', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    token: localStorage.getItem('token')
                }
            })
            const userData = await userResponse.json()
            if (userResponse.ok) {
                usernameFormElement.value = userData.username
                emailFormElement.value = userData.email
                nameFormElement.value = userData.name
                rollFormElement.value = userData.roll
                phoneFormElement.value = userData.phone
                branchFormElement.value = userData.branch
                addressFormElement.value = userData.address
            } else {
                throw new Error(userData.message)
            }
        } else {
            throw new Error(data.message)
        }
    } catch (e) {
        alert(e.message)
        window.location.reload()
    }
})

const updateButton = document.getElementById('update')

updateButton.addEventListener('click', async (e) => {
    e.preventDefault()
    try {
        const response = await fetch('http://localhost:3000/edit/editDetails', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                token: localStorage.getItem('token'),
                otptoken: localStorage.getItem('otptoken')
            },
            body: JSON.stringify({
                email: emailFormElement.value,
                name: nameFormElement.value,
                roll: rollFormElement.value,
                phone: phoneFormElement.value,
                branch: branchFormElement.value,
                address: addressFormElement.value
            })
        })
        // const data = await response.json()
        if (response.ok) {
            window.location.href = 'user.html'
        } else {
            throw new Error(data.message)
        }
    } catch (e) {
        alert(e.message)
    }
})
