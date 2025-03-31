$(document).ready(function () {
    // Sidebar toggle
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
        $('#content').toggleClass('active');
    });

    // Active menu item highlighting
    const currentPath = window.location.pathname;
    $('.sidebar .nav-link').each(function () {
        if ($(this).attr('href') === currentPath) {
            $(this).addClass('active');
        }
    });

    // Card hover effects
    $('.card').hover(
        function () {
            $(this).css('transform', 'translateY(-5px)');
            $(this).css('box-shadow', '0 4px 8px rgba(0,0,0,0.15)');
        },
        function () {
            $(this).css('transform', 'translateY(0)');
            $(this).css('box-shadow', '0 2px 4px rgba(0,0,0,0.1)');
        }
    );

    // Form handling
    $('form').on('submit', function (e) {
        e.preventDefault();

        // Get form data
        const formData = $(this).serialize();

        // Send AJAX request
        $.ajax({
            url: $(this).attr('action'),
            method: $(this).attr('method') || 'POST',
            data: formData,
            success: function (response) {
                // Handle success
                showNotification('Success!', 'success');
            },
            error: function (xhr) {
                // Handle error
                showNotification('Error occurred!', 'error');
            }
        });
    });

    // Date handling
    $('.date-input').each(function () {
        // Initialize datepicker if present
        if ($(this).hasClass('datepicker')) {
            $(this).datepicker({
                dateFormat: 'yy-mm-dd',
                changeMonth: true,
                changeYear: true,
                yearRange: '-100:+0'
            });
        }

        // Format date on input
        $(this).on('input', function () {
            const date = $(this).val();
            if (date) {
                const formattedDate = formatDate(date);
                $(this).val(formattedDate);
            }
        });
    });

    // Notification system
    function showNotification(message, type) {
        const notification = $('<div>')
            .addClass(`alert alert-${type} notification`)
            .text(message)
            .appendTo('body');

        setTimeout(() => {
            notification.fadeOut(() => notification.remove());
        }, 3000);
    }

    // Date formatting helper
    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toISOString().split('T')[0];
    }

    // Form validation
    $('form').each(function () {
        $(this).find('input[required]').each(function () {
            $(this).on('blur', function () {
                if (!$(this).val()) {
                    $(this).addClass('is-invalid');
                } else {
                    $(this).removeClass('is-invalid');
                }
            });
        });
    });

    // Dynamic form fields
    $('.add-field').on('click', function () {
        const template = $(this).data('template');
        const container = $(this).prev('.dynamic-fields');
        container.append(template);
    });

    // Remove dynamic fields
    $(document).on('click', '.remove-field', function () {
        $(this).closest('.dynamic-field').remove();
    });
}); 