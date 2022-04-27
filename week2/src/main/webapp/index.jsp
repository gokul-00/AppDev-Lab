<%
	if(session.getAttribute("name")==null){
		response.sendRedirect("login.jsp");
	}
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<script src="https://use.fontawesome.com/releases/v5.15.4/js/all.js"
	crossorigin="anonymous"></script>
<!-- Google fonts-->
<link href="https://fonts.googleapis.com/css?family=Montserrat:400,700"
	rel="stylesheet" type="text/css" />
<link
	href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic"
	rel="stylesheet" type="text/css" />
<!-- Core theme CSS (includes Bootstrap)-->
<link href="css/index-styles.css" rel="stylesheet" />
<link href="css/course.css" rel="stylesheet">
<link rel="stylesheet" href="alert/dist/sweetalert.css">
</head>
<body id="page-top">

<input type="hidden" id="status" value="<%= request.getAttribute("status") %>">
	<!-- Navigation-->
	<nav
		class="navbar navbar-expand-lg bg-primary text-uppercase fixed-top"
		id="mainNav">
		<div class="container">
			<a class="navbar-brand" href="#page-top">Course Registration</a>
			<button
				class="navbar-toggler text-uppercase font-weight-bold bg-primary text-white rounded"
				type="button" data-bs-toggle="collapse"
				data-bs-target="#navbarResponsive" aria-controls="navbarResponsive"
				aria-expanded="false" aria-label="Toggle navigation">
				Menu <i class="fas fa-bars"></i>
			</button>
			<div class="collapse navbar-collapse" id="navbarResponsive">
				<ul class="navbar-nav ms-auto">
				<li class="nav-item mx-0 mx-lg-1"><a
						class="nav-link py-3 px-0 px-lg-3 rounded" href="">hi, <%= session.getAttribute("name")%></a></li>	
					<li class="nav-item mx-0 mx-lg-1 bg-danger"><a
						class="nav-link py-3 px-0 px-lg-3 rounded" href="Logout">Logout</a></li>			
				</ul>
			</div>
		</div>
	</nav>
	
	<div class="col-md-6 mb-4 mx-auto" style="margin-top:10%;">
                    <div class="card">
                        <div class="card-body">
                            <!-- Form register -->
                            <form method="post" action="course" class="register-form"
							id="register-form">
                                <h2 class="text-center font-up font-bold deep-orange-text py-4">Course</h2>
                                <div class="md-form">
                                    <label for="username">username</label>
                                    <input type="text" name="username" id="username" class="form-control" value="<%= session.getAttribute("name") %>" readonly="true">
                                </div>
                                <div class="md-form">
                                    <label for="rollno">Roll number</label>
                                    <input type="text" name="rollno" id="rollno" class="form-control">
                                </div>
                                <div class="md-form">
                                    <label for="year">year</label>
                                    <input type="number" max=5 min=1 name="year" id="year" class="form-control">
                                </div>
                                <div class="md-form">
                                    <label for="semester">semester</label>
                                    <input type="number" max=10 min=1 name="semester" id="semester" class="form-control">
                                </div>
                                <div class="md-form">
                                    <label for="course">course</label>
                                    <input type="text" name="course" id="course" class="form-control">
                                </div>
                                <div class="text-center">
                                    <button type="submit" class="bg-secondary px-3 py-1 my-3 text-white rounded border-0">Submit</button>
                                </div>
                            </form>
                            <!-- Form register -->
                        </div>
                    </div>
                </div>
                <!-- Grid column -->
            </div>
	
	
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
	<!-- Core theme JS-->
	<script src="js/scripts.js"></script>
	<script src="https://cdn.startbootstrap.com/sb-forms-latest.js"></script>
		<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
	
<script type="text/javascript">
	var status = document.getElementById("status").value;
	if(status == "success"){
		swal("DONE","Course Registration successful!", "success");
	}
	if(status == "failed"){
		swal("ERROR","Registration failed :(", "error");
	}
</script>
</body>
</html>
