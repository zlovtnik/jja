$(document).ready(function () {
    // Add a click event to the content div
    $('#content').click(function () {
        $(this).css('background-color', '#f0f0f0');
    });

    // Add a hover effect to the heading
    $('h1').hover(
        function () {
            $(this).css('color', '#007bff');
        },
        function () {
            $(this).css('color', '#333');
        }
    );

    // Add a new paragraph when clicking the content
    $('#content').click(function () {
        $(this).append('<p>New paragraph added with jQuery!</p>');
    });
}); 