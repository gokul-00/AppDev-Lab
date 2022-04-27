const form = document.getElementById('form');
const firstname = document.getElementById('firstname');
const lastname = document.getElementById('lastname');
const email = document.getElementById('email');
const password = document.getElementById('password');
// const gender = document.getElementsByName('gender');
const mobile = document.getElementById('mobile');
const address = document.getElementById('address');
const state = document.getElementById('state');
const city = document.getElementById('city');
const country = document.getElementById('country');
const submitbtn = document.getElementById('submit');
const clearbtn = document.getElementById('clear');

const male = document.getElementById('male');
const female = document.getElementById('female');

function showError(input, message) {
	const formInput = input.parentElement;
	formInput.className = 'form-input error';
	const small = formInput.querySelector('small');
	small.innerText = message;
}

function showSucces(input) {
	const formInput = input.parentElement;
	formInput.className = 'form-input success';
	const small = formInput.querySelector('small');
	small.innerText = '';
}

function getFieldName(input) {
	return input.id.charAt(0).toUpperCase() + input.id.slice(1);
}

function checkEmail(input) {
	const re =
		/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	if (re.test(input.value.trim())) {
		showSucces(input);
	} else {
		showError(input, 'Email is invalid');
	}
}

function checkPassword(input) {
	const re = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,25}$/;
	if (re.test(input.value.trim())) {
		showSucces(input);
	} else {
		showError(input, 'Password is invalid');
	}
}

function checkMobile(mobile) {
	if (mobile.value.trim().length == 10) {
		showSucces(mobile);
	} else {
		showError(mobile, `${getFieldName(mobile)} is invalid`);
	}
}
function checkGender() {
	if (male.value.trim() && male.checked) {
		console.log(getFieldName(male), male.value.trim());
		showSucces(male);
	} else if (female.value.trim() && female.checked) {
		console.log(getFieldName(female), female.value.trim());
		showSucces(female);
	} else {
		showError(male, `Gender is required`);
	}
}

function checkRequired(inputArr) {
	inputArr.forEach(function (input) {
		console.log(getFieldName(input), input.value.trim());
		if (input.value.trim() === '') {
			showError(input, `${getFieldName(input)} is required`);
		} else {
			showSucces(input);
		}
	});
}

form.addEventListener('submit', function (e) {
	e.preventDefault();

	checkRequired([
		firstname,
		lastname,
		email,
		mobile,
		address,
		country,
		state,
		city,
		password,
	]);
	checkGender();
	checkEmail(email);
	checkMobile(mobile);
	checkPassword(password);
});

function clearInputs() {
	const arr = [
		firstname,
		lastname,
		email,
		mobile,
		address,
		country,
		state,
		city,
		password,
	];
	arr.forEach((ele) => {
		const formInput = ele.parentElement;
		formInput.className = 'form-input';
		const input = formInput.querySelector('input');
		input.value = '';
	});
}

clearbtn.addEventListener('click', function (e) {
	clearInputs();
});
