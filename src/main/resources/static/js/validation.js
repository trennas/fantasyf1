var addressChanged = false;
var postCodeValid = false;

var delay = (function() {
    var timer = 0;
    return function(callback, ms) {
        clearTimeout(timer);
        timer = setTimeout(callback, ms);
    };
})();

function validatePayment() {
    var payment = {
        'firstName' : $.trim($('#txtFirstName').val()),
        'surname' : $.trim($('#txtSurname').val()),
        'policyHolderName' : $.trim($('#txtPolicyHolderName').val()),
        'policyNumber' : $.trim($('#txtPolicyNumber').val()),
        'policyIndex' : $.trim($('#txtPolicyIndex').val()),
        'policyCheckLetter' : $.trim($('#txtPolicyCheckLetter').val()),
        'networkName' : $.trim($('#txtNetworkName').val()),
        'agencyName' : $.trim($('#txtAgencyName').val()),
        'agencyNumber' : $.trim($('#txtAgencyNumber').val()),
        'amount' : $.trim($('#txtAmount').val().replace(/,/g, '')),
        'address' : {
            'orgName' : $.trim($('#txtOrgName').val()),
            'houseName' : $.trim($('#txtHouseName').val()),
            'houseNumber' : $.trim($('#txtHouseNumber').val()),
            'streetName' : $.trim($('#txtStreet').val()),
            'city' : $.trim($('#txtCity').val()),
            'county' : $.trim($('#txtCounty').val()),
            'postCode' : $.trim($('#txtPostcode').val().toUpperCase())
        }
    };
    validate('/payment/validatePayment?paymentType='
            + $("#paymentTypeField").val(), payment);
}

function validate(url, data) {
    var errors = 0;
    postCodeValid = true;
    $('.errorRow').hide();
    $('.requiredField').hide();
    $.ajax({
        type : "POST",
        url : url,
        dataType : 'json',
        async : false,
        contentType : "application/json;charset=utf-8",
        data : JSON.stringify(data),
        success : function(data) {
            for (var fieldName in data) {
                errors++;
                var message = data[fieldName];
                if (fieldName.toLowerCase().indexOf("address_") !== -1) {
                    if(fieldName.toLowerCase().indexOf("postcode") === -1) {
                        $('.genericAddressError').show();
                    } else {
                        postCodeValid = false;
                    }
                }

                if (message.match(/required/i) ||
                  $('#txt' + fieldName.capitalizeFirstLetter()).val() === '') {
                    $('.' + fieldName + "RequiredField").show();
                } else {
                    $('.' + fieldName + "InvalidText").text(message);
                    $('.' + fieldName + "InvalidRow").show();
                }
            }
            if (errors === 0) {
                $('.validatedSubmitButton').removeAttr('disabled');
            }

            var addressSelectDropdown = $('#addressSelectDropdown');
            if (addressChanged){
                addressChanged = false;
                if(postCodeValid) {
                    lookupAddresses();                    
                }
            }
        }
    });
    return errors;
}

String.prototype.capitalizeFirstLetter = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
};