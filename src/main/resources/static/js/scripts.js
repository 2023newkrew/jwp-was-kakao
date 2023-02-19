String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};

$(document).ready(function() {
  $("#loginForm").submit(function(event) {
    event.preventDefault();
    const formData = $(this).serialize();
    $.ajax({
      type: "POST",
      url: "/user/login",
      data: formData,
      success: function(response) {
        window.location.href = "/index.html";
      },
      error: function(response) {
        window.location.href = "/user/login.html";
      }
    });
  });
});