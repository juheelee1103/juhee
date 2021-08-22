<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>firstspring</title>
<style type="text/css">
table th { background-color : #99ffff; }
table#outer { border : 2px solid navy;  }
</style>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/resources/js/jquery-3.6.0.min.js"></script>
<script>
	function validate(){
		//ì í¨ì± ê²ì¬ ì½ë ìì±í¨
		//ìë² ì»¨í¸ë¡¤ë¬ë¡ ì ì¡í  ê°ë¤ì´ ìêµ¬í ì¡°ê±´ì ëª¨ë ë§ì¡±íìëì§ ê²ì¬í¨

		//ìí¸ì ìí¸ íì.¸ì´ ì¼ì¹íì§ ìëì§ íì¸í¨
		var pwdValue = document.getElementById("userpwd").value;
		var pwdValue2 = document.getElementById("userpwd2").value;

		if(pwdValue !== pwdValue2){
			alert("ìí¸ì ìí¸ íì¸ì ê°ì´ ì¼ì¹íì§ ììµëë¤.");
			document.getElementById("userpwd").select();
			return false;  //ì ì¡ ì·¨ìí¨
		}

		return true;  //ì ì¡í¨
	}
	
	//ìì´ë ì¤ë³µ ì²´í¬ íì¸ì ìí ajax ì¤í ì²ë¦¬ì© í¨ì
	function dupIdCheck(){
		console.log("dup");
		$.ajax({
			url: "idchk.do",
			type: "post",
			data: {userid: $("#userid").val()},
			success: function(data){
				console.log("success : " + data);
				if(data == "ok"){
					alert("ì¬ì© ê°ë¥í ìì´ëìëë¤.");
					$("#userpwd").focus();
				}else{
					alert("ì´ë¯¸ ì¬ì©ì¤ì¸ ìì´ëìëë¤.\në¤ì ìë ¥íì¸ì.");
					$("#userid").select();
				}
			},
			error: function(jqXHR, textstatus, errorthrown){
				console.log("error : " + jqXHR + ", " + textstatus
						+ ", " + errorthrown);
			}
		});
		
		return false;  //í´ë¦­ ì´ë²¤í¸ê° ì ë¬ëì´ì submit ì´ ëìëì§ ìê² í¨
	}
</script>
</head>
<body>
<center>
<h1 align="center">íì ê°ì íì´ì§</h1>
<br>
<form method="post" action="enroll.do" onsubmit="return validate();">
<table id="outer" align="center" width="500" cellspacing="5" cellpadding="0">
<tr>
	<th colspan="2">íì ì ë³´ë¥¼ ìë ¥í´ ì£¼ì¸ì. (* íìë íììë ¥ í­ëª©ìëë¤.)</th>	
</tr>
<tr>
	<th width="120">*ì´ ë¦</th>
	<td><input type="text" name="username" required></td>
</tr>
<tr>
	<th>*ìì´ë</th>
	<td><input type="text" name="userid" id="userid" required> &nbsp; 
	<input type="button" value="중복체크" onclick="return dupIdCheck();"></td>
</tr>
<tr>
	<th>*ì í¸</th>
	<td><input type="password" name="userpwd" id="userpwd" required></td>
</tr>
<tr>
	<th>*ìí¸íì¸</th>
	<td><input type="password" id="userpwd2"></td>
</tr>
<tr>
	<th>*ì± ë³</th>
	<td><input type="radio" name="gender" value="M" checked> ë¨ì &nbsp; 
	    <input type="radio" name="gender" value="F"> ì¬ì</td>
</tr>
<tr>
	<th>*ë ì´</th>
	<td><input type="number" name="age" min="19" max="200" value="20"></td>
</tr>
<tr>
	<th>*ì íë²í¸</th>
	<td><input type="tel" name="phone" required></td>
</tr>
<tr>
	<th>*ì´ë©ì¼</th>
	<td><input type="email" name="email" required></td>
</tr>
<tr>
	<th>ì·¨ ë¯¸</th>
	<td>
		<table width="350">
		<tr>
			<td><input type="checkbox" name="hobby" value="game"> ê²ì</td>
			<td><input type="checkbox" name="hobby" value="reading"> ëì</td>
			<td><input type="checkbox" name="hobby" value="climb"> ë±ì°</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="hobby" value="sport"> ì´ë</td>
			<td><input type="checkbox" name="hobby" value="music"> ììë£ê¸°</td>
			<td><input type="checkbox" name="hobby" value="movie"> ìíë³´ê¸°</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="hobby" value="travel"> ì¬í</td>
			<td><input type="checkbox" name="hobby" value="cook"> ìë¦¬</td>
			<td><input type="checkbox" name="hobby" value="etc" checked> ê¸°í</td>
		</tr>
		</table>
	</td>
</tr>
<tr>
	<th>íê³ ì¶ì ë§</th>
	<td><textarea name="etc" rows="5" cols="50"></textarea></td>
</tr>
<tr>
	<th colspan="2">
		<input type="submit" value="ê°ìíê¸°"> &nbsp; 
		<input type="reset" value="ìì±ì·¨ì"> &nbsp; 
		<a href="main.do">ìì íì´ì§ë¡</a>
	</th>	
</tr>
</table>
</form>
</center>

</body>
</html>