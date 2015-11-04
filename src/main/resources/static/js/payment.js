// Expand iframe if returning from payment gateway.
if (window.self !== window.top) {
	top.location = window.location;
}

$(function() {
	validatePayment();
	$("#paymentType li").click(function() {
		var opt = $(this).attr('data-form');
		$('#container').css("min-height", $('#container').height());
		$("#paymentForm").hide();
		// Hide all conditional fields then show fields for selected payment
		// type.
		$(".conditionalField").hide();
		$('.conditionalField input[type="text"]').val('');
		$("." + opt + "_Field").show();
		$("#paymentForm").toggle("slide");
		// Set the payment type param ensuring it's maintained for when
		// validation errors need to be displayed.
		$("#paymentTypeField").attr("value", opt);
		validatePayment();
	});
	// Set initial payment type.
    var first = $('ul#paymentType li:first');
    var paymentType = first.attr('data-form');
	$("#paymentType li[data-form='" + paymentType + "']")[0].click();
	$('#txtAmount').autoNumeric('init', {
		vMin : 0
	});
	// ADD SLIDEDOWN ANIMATION TO DROPDOWN //
	$('.dropdown').on('show.bs.dropdown', function() {
		$(this).find('.dropdown-menu').first().stop(true, true).slideDown();
	});
	// ADD SLIDEUP ANIMATION TO DROPDOWN //
	$('.dropdown').on('hide.bs.dropdown', function() {
		$(this).find('.dropdown-menu').first().stop(true, true).slideUp();
	});

	$('input').keyup(function() {
	    $('.validatedSubmitButton').attr('disabled', 'disabled');
		delay(function() {
			validatePayment();
		}, 750);
	});
	$("#txtPolicyNumber, #txtPolicyIndex, #txtPolicyCheckLetter").keyup(
			function() {
				if ($("#txtPolicyNumber").val().length < 1
						|| $("#txtPolicyIndex").val().length < 1
						|| $("#txtPolicyCheckLetter").val().length < 1) {
					$("#txtPolicyId").val("");
				} else {
					$("#txtPolicyId").val(
							$("#txtPolicyNumber").val() + "-"
									+ $("#txtPolicyIndex").val() + "-"
									+ $("#txtPolicyCheckLetter").val());
				}
			});
});