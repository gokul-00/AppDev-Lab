let editStu = false;
const elements = document.getElementsByClassName('form-check-input');
for (let i = 0; i < elements.length; i++) {
	elements[i].addEventListener('change', () => {
		const xhttp = new XMLHttpRequest();
		xhttp.onload = () => {
			const data = JSON.parse(xhttp.responseText);
			let val = document.getElementById('price');
			if (elements[i].checked) {
				val.value = parseInt(val.value) + data.details.price;
			} else {
				val.value -= data.details.price;
			}
		};
		xhttp.open(
			'GET',
			`http://localhost:3001/events?eventName=${elements[i].value}`
		);
		xhttp.send();
	});
}
document.getElementById('add').addEventListener('click', () => {
	submit(editStu);
});
const submit = (edit) => {
	const xhttp = new XMLHttpRequest();

	xhttp.open('POST', `http://localhost:3001/add`);
	xhttp.setRequestHeader('Content-type', 'application/json');
	xhttp.onload = () => {
		if (xhttp.status == 200) {
			alert('Successfully added');

			refresh(false);
		} else alert(JSON.parse(xhttp.responseText).message);
	};

	const events = [];
	for (let i = 0; i < elements.length; i++) {
		if (elements[i].checked) {
			events.push(elements[i].value);
		}
	}
	const eve = JSON.stringify(events);
	const formSubmit = {
		name: document.getElementById('name').value,
		rollNo: document.getElementById('roll').value,
		course: document.getElementById('course').value,
		events: eve,
		edit,
	};
	const sub = JSON.stringify(formSubmit);
	xhttp.send(sub);
};

const refresh = (del) => {
	const xhttp = new XMLHttpRequest();
	xhttp.onload = () => {
		const data = JSON.parse(xhttp.responseText);
		document.getElementById('inner').innerHTML = '';
		for (let i = 0; i < data.details.length; i++) {
			const eventsArr = JSON.parse(data.details[i].event);
			let eventsStr = '';
			for (let i = 0; i < eventsArr.length; i++)
				eventsStr += eventsArr[i] + ', ';

			document.getElementById('inner').innerHTML += `<tr>
                <th scope="row">${i}</th>
                <td class="name">${data.details[i].name}
                  </td>
                <td class="rollno">${data.details[i].rollNo}</td>
                <td class="course">${data.details[i].course}</td>
                <td class="event">${eventsStr}</td>

                <td><button class="btn btn-primary edit">Edit</button></td>
                <td><button class="btn btn-danger delete">Delete</button></td>
              </tr>`;
			editFun();
			if (!del) deleteFun();
		}
	};
	xhttp.open('GET', `http://localhost:3001/all`);
	xhttp.send();
};
const editFun = () => {
	let edit = document.getElementsByClassName('edit');
	for (let i = 0; i < edit.length; i++) {
		edit[i].addEventListener('click', () => {
			document.getElementById('name').value =
				document.getElementsByClassName('name')[i].innerHTML;
			document.getElementById('roll').value =
				document.getElementsByClassName('rollno')[i].innerHTML;
			document.getElementById('roll').readOnly = true;
			document.getElementById('course').value =
				document.getElementsByClassName('course')[i].innerHTML;
			document.getElementById('price').value = 0;
			for (let j = 0; j < elements.length; j++) elements[j].checked = false;

			editStu = true;
		});
	}
};
const deleteFun = () => {
	let del = document.getElementsByClassName('delete');
	for (let i = 0; i < del.length; i++) {
		del[i].addEventListener('click', () => {
			const xhttp = new XMLHttpRequest();
			xhttp.open(
				'DELETE',
				`http://localhost:3001/stud_del?rollNo=${
					document.getElementsByClassName('rollno')[i].innerHTML
				}`
			);
			xhttp.onload = () => {
				if (xhttp.status == 200) {
					alert('Successfully deleted');
					refresh(true);
				}
			};
			xhttp.send();
		});
	}
};
refresh(false);
document.getElementById('clear').addEventListener('click', () => {
	document.getElementById('name').value = '';
	document.getElementById('roll').value = '';
	document.getElementById('roll').readOnly = false;
	document.getElementById('course').value = '';
	document.getElementById('price').value = 0;
	for (let i = 0; i < elements.length; i++) elements[i].checked = false;
	editStu = false;
});

const feeCalc = (e) => {
	console.log('check');
	// if (e.target.checked) document.getElementById('price').value += 100;
};
