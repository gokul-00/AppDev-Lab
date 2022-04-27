const usernameElement = document.getElementById('username')
const emailElement = document.getElementById('email')
const nameElement = document.getElementById('name')
const rollElement = document.getElementById('roll')
const passwordElement = document.getElementById('password')
const confirmPasswordElement = document.getElementById('cpassword')
const phoneElement = document.getElementById('phone')
const branchElement = document.getElementById('branch')
const addressEleemnt = document.getElementById('address')

const submitButton = document.getElementById('submit')

submitButton.addEventListener('click', async (e) => {
    e.preventDefault()
    const username = usernameElement.value
    const email = emailElement.value
    const name = nameElement.value
    const roll = rollElement.value
    const password = passwordElement.value
    const cpassword = confirmPasswordElement.value
    const phone = phoneElement.value
    const branch = branchElement.value
    const address = addressEleemnt.value
    const details = {
        username,
        email,
        name,
        roll,
        password,
        cpassword,
        phone,
        branch,
        address
    }
    try {
        const response = await fetch('http://localhost:3000/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(details)
        })
        // const data = await response.json()
        if (response.ok) {
            window.location.href = 'login.html'
        } else {
            throw new Error(data.message)
        }
    } catch (e) {
        alert(e.message)
    }
})
