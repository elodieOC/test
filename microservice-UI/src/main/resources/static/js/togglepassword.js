$(document).ready(function() {
    $("#password-eye").on('click', function(event) {
        event.preventDefault();
        if($('#password').attr("type") == "text"){
            $('#password').attr('type', 'password');
            $('#eye-icon').removeClass( "glyphicon glyphicon-eye-open" );
            $('#eye-icon').addClass( "glyphicon glyphicon-eye-close" );
        }else if($('#password').attr("type") == "password"){
            $('#password').attr('type', 'text');
            $('#eye-icon').removeClass( "glyphicon glyphicon-eye-close" );
            $('#eye-icon').addClass( "glyphicon glyphicon-eye-open" );
        }
    });
});
$('#password-eye').keypress(
    function(event){
        if (event.which == '13') {
            event.preventDefault();
        }
    });