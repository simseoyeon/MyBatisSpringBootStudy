//var url = new URL(window.location.href);
var error = url.searchParams.get('error');
alert(error);
if (error != null) {
	document.getElementById('msg').innerText = "아이디 또는 패스워드가 잘못되었습니다.";
}


//아이디체크
function checkId() {
	var snd_data = $("#userid").val();
	$.ajax(
		{
			type: "get",
			dataType: "text",
			async: true, //true : 비동기방식 - 페이지 갱신 X
			url: "http://localhost:8090/checkid",
			data: { data: snd_data }, //뷰에서 서버로 넘어가는 데이터
			success: function(data, textStatus) {
				if (data == "true") {
					$("#id-area").html("<p>사용 가능한 아이디입니다.</p>");
				} else {
					$("#id-area").html("<p>사용할 수 없는 아이디입니다.</p>");
				}

			},
			error: function(data, textStatus) {
				window.alert("에러가 발생했습니다.");
				window.alert(textStatus);
			},
			complete: function(data, textStatus) {
				//window.alert("작업을 완료했습니다.");
			}
		}
	);
}

function checkLoginForm() {
	//유효성 검사
	const form = document.loginpage;
	const username = document.getElementById('userid').value;
	const userpwd = document.getElementById('userpwd').value;

	if (username.length < 3 || username.length > 8) {
		window.alert("아이디는 3~8자로 입력해주세요.");
		form.username.focus();
		return;
	}
	if (userpwd.length < 4 || userpwd.length > 20) {
		window.alert("패스워드는 4~20자로 입력해주세요.");
		form.userpwd.focus();
		return;
	}

	window.alert("로그인이 완료되었습니다.");
	form.submit(); //서버 연동
}


function checkJoinForm() {
	//유효성 검사
	const form = document.joinForm;
	const userid = document.getElementById('userid').value;
	const userpwd = document.getElementById('userpwd').value;
	const username = document.getElementById('username').value;
	const birthdate = document.getElementById('birthdate').value;
	const genderM = document.getElementById('genderM').checked;
	const genderF = document.getElementById('genderF').checked;
	const telnumber = document.getElementById('telnumber').value;
	const addr = document.getElementById('addr').value;

	if (userid.length < 3 || userid.length > 8) {
		window.alert("아이디는 3~8자로 입력해주세요.");
		form.userid.focus();
		return;
	}
	if (userpwd.length < 4 || userpwd.length > 20) {
		window.alert("패스워드는 4~20자로 입력해주세요.");
		form.userpwd.focus();
		return;
	}
	if (username.value == "") {
		window.alert("이름을 입력해주세요.");
		form.username.focus();
		return;
	}
	if (birthdate.value == "") {
		window.alert("생년월일을 입력해주세요.");
		form.birthdate.focus();
		return;
	}
	if (!genderM && !genderF) {
		window.alert("성별을 선택해주세요.");
		return;
	}
	if (telnumber.value == "") {
		window.alert("전화번호를 입력해주세요.");
		form.telnumber.focus();
		return;
	}
	if (addr.value == "") {
		window.alert("주소를 입력해주세요.");
		form.addr.focus();
		return;
	}

	window.alert("회원가입이 완료되었습니다.");
	form.submit(); //서버 연동
	//location.href="./index1.html";
}


function checkFormJquery() {
	const form = $('#joinForm');
	const userid = $('#userid').val();
	const userpwd = $('#userpwd').val();
	const username = $('#username').val();
	const birthdate = $('#birthdate').val();
	const genderM = $('#genderM').is(':checked');
	const genderF = $('#genderF').is(':checked');
	const telnumber = $('#telnumber').val();
	const addr = $('#addr').val();

	if (userid.length < 3 || userid.length > 8) {
		window.alert("아이디는 3~8자로 입력해주세요.");
		$('#userid').focus();
		return;
	}
	if (userpwd.length < 4 || userpwd.length > 20) {
		window.alert("패스워드는 4~20자로 입력해주세요.");
		$('#userpwd').focus();
		return;
	}
	if (username.value == "") {
		window.alert("이름을 입력해주세요.");
		$('#username').focus();
		return;
	}
	if (birthdate.value == "") {
		window.alert("생년월일을 입력해주세요.");
		$('#birth').focus();
		return;
	}
	if (!genderM && !genderF) {
		window.alert("성별을 선택해주세요.");
		return;
	}
	if (telnumber.value == "") {
		window.alert("전화번호를 입력해주세요.");
		$('#telnumber').focus();
		return;
	}
	if (addr.value == "") {
		window.alert("주소를 입력해주세요.");
		$('#addr').focus();
		return;
	}

}

//회원정보
function checkMemberForm() {
	//유효성 검사
	const form = document.memberForm;

	window.alert("회원가입이 수정되었습니다.");
	form.submit(); //서버 연동
}

//회원 관리 - 수정
function userSubmit(button){
	var userid = button.getAttribute('data-userid');
	var permitEl = button.parentElement.previousElementSibling.firstElementChild;
	var userpwdEl = permitEl.parentElement.previousElementSibling.firstElementChild;
	var userpwd = userpwdEl.value;
	var permit = permitEl.value;
	console.log(userpwd);
	console.log(permit);
	
	var form = document.createElement('form');
	form.action = 'editUser';
	form.method = 'post';
	
	var useridInput = document.createElement('input');
	useridInput.type = 'hidden';
	useridInput.name = 'userid';
	useridInput.value = userid;
	
	var userpwdInput = document.createElement('input');
	userpwdInput.type = 'hidden';
	userpwdInput.name = 'userpwd';
	userpwdInput.value = userpwd;
	
	var permitInput = document.createElement('input');
	permitInput.type = 'hidden';
	permitInput.name = 'permit';
	permitInput.value = permit;
	
	form.appendChild(useridInput);
	form.appendChild(userpwdInput);
	form.appendChild(permitInput);
	
	document.body.appendChild(form);
	form.submit();
}

function deleteUser(button){
	var userid = button.getAttribute('data-userid');
	location.href = "/deleteUser/" + userid;
	
}

//(관리자 권한)게시판 수정
function editBoard(button){
	var boardno = button.getAttribute('data-boardno');
	var descriptEl = button.parentElement.previousElementSibling.firstElementChild;
	var boardnameEl = descriptEl.parentElement.previousElementSibling.firstElementChild;
	var descript = descriptEl.value;
	var boardname = boardnameEl.value;
	console.log(descript);
	console.log(boardname);
	
	var form = document.createElement('form');
	form.action = '/editBoard'; //다음 이동 페이지 requestmapping이름
	form.method = 'post';
	
	var boardnoInput = document.createElement('input');
	boardnoInput.type = 'hidden';
	boardnoInput.name = 'board_no';
	boardnoInput.value = boardno;
	
	var boardnameInput = document.createElement('input');
	boardnameInput.type = 'hidden';
	boardnameInput.name = 'board_name';
	boardnameInput.value = boardname;
	
	var descriptInput = document.createElement('input');
	descriptInput.type = 'hidden';
	descriptInput.name = 'descript';
	descriptInput.value = descript;
	
	form.appendChild(boardnoInput);
	form.appendChild(boardnameInput);
	form.appendChild(descriptInput);
	
	document.body.appendChild(form);
	form.submit();
}

function deleteBoard(button){
	var boardno = button.getAttribute('data-boardno');
	location.href = "/deleteBoard/" + boardno;
	
}

function sendMail(){
    const form = document.inquireForm;
    var subject;
    const to = "simseo034@gmail.com";
    var text;

    subject = form.name.value + "님이 문의하신 내용입니다.";
//    text = form.innerHTML;
    text=`
    <table>
    <tr><td>제목</td><td>${form.title.value}</td></tr>
    <tr><td>이메일</td><td>${form.email.value}</td></tr>
    <tr><td>이름</td><td>${form.name.value}</td></tr>
    <tr><td>전화번호</td><td>${form.telno.value}</td></tr>
    <tr><td>내용</td><td>${form.content.value}</td></tr>
    </table>
    `

    var subjectInput = document.createElement('input');
    subjectInput.type = 'hidden';
    subjectInput.name = 'subject';
    subjectInput.value = subject;

    var toInput = document.createElement('input');
    toInput.type = 'hidden';
    toInput.name = 'to';
    toInput.value = to;

    var textInput = document.createElement('input');
    textInput.type = 'hidden';
    textInput.name = 'text';
    textInput.value = text;

    form.appendChild(subjectInput);
    form.appendChild(toInput);
    form.appendChild(textInput);

    form.submit();
}

function findPw(){
    window.open('/findpw','비밀번호 찾기','width=420, height=400, history=no, status=no, menubar=no, scrollbars=no, resizable=no');
}

function findId(){
	window.open('/findid','아이디 찾기',
	'width=420, height=400, history=no, resizable=no, scrollbars=no, menubar=no');
}

function delFile(button){
	button.parentElement.remove();
}

function addFile(button) {
	const parent = button.parentElement;
	var addEl = document.createElement('p');
	addEl.innerHTML = '<input type="file" name="file">'
					+ '<input type="button" value="X" onclick="delFile(this)">';
	parent.appendChild(addEl);
}

function addImg(button) {
	const parent = button.parentElement;
	var addIm = document.createElement('p');
	addIm.innerHTML = '<input type="file" name="file">'
					+ '<input type="checkbox">대표이미지'
					+ '<input type="button" value="X" onclick="delFile(this)">';
	parent.appendChild(addIm);
}

// 대표이미지 체크
function galarySubmit() {
	var chkbxs = document.querySelectorAll('input[type="checkbox"]');
	for(var i=0; i<chkbxs.length; i++) {
		var thumbnailEl = document.createElement('input');
		thumbnailEl.type = 'hidden';
		thumbnailEl.name = 'thumbnail';
		thumbnailEl.value = chkbxs[i].checked ? '1' : '0';
		chkbxs[i].parentElement.parentElement.appendChild(thumbnailEl);
	}
	document.galaryForm.submit();
}

/*// 페이지 이동
function movePage(page) {

	// 1. 검색 폼
	const form = document.getElementById('searchForm');

	// 2. drawPage( )의 각 버튼에 선언된 onclick 이벤트를 통해 전달받는 page(페이지 번호)를 기준으로 객체 생성
	const queryParams = {
		page: (page) ? page : 1,
		recordSize: 5,
		pageSize: 5,
		searchType: form.searchType.value,
		keyword: form.keyword.value
	}


	 3. location.pathname : 리스트 페이지의 URI("/post/list.do")를 의미
	    new URLSearchParams(queryParams).toString() : queryParams의 모든 프로퍼티(key-value)를 쿼리 스트링으로 변환
	    URI + 쿼리 스트링에 해당하는 주소로 이동
	    (해당 함수가 리턴해주는 값을 브라우저 콘솔(console)에 찍어보시면 쉽게 이해하실 수 있습니다.)

	location.href = location.pathname + '?' + new URLSearchParams(queryParams).toString();
}*/

// 페이징 검색기능
function search(button){
	var boardno = button.getAttribute('data-boardno');
	var keyword = document.getElementById("keyword").value;
	location.href="/board/" + boardno + "?keyword=" + keyword;
}