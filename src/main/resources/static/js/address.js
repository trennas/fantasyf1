$(document).ready(function() {
    if ($('#formattedAddress').val().length > 0) {
        var addressSelectDropdown = $('#addressSelectDropdown');
        var cantFind = addressSelectDropdown.find('option[value="Cant_find"]');
        var string = '<option value="" class="generated" selected="selected">' + $('#formattedAddress').val() + '</option>';
        addressSelectDropdown.find("option").prop("selected", false);
        cantFind.before(string);
        addressSelectDropdown.removeAttr('disabled');
    }
    $("#addressSelectDropdown").change(function() {
        $('.validatedSubmitButton').attr('disabled', 'disabled');
        var selectedAddress = $(this).val();
        if (selectedAddress === 'Cant_find') {
            // unselect "Can't find" option
            $(this).val('');
            $('#modalAddress').modal();
        } else if (selectedAddress !== 'selectAddress') {
            // populate hidden fields
            updateAddressFields(decode(selectedAddress));
        }
        validatePayment();
    });

    $('#manualAddressSubmit').click(function() {       
        validatePayment();
        if(!$('.genericAddressError').is(':visible')) {
            $('.validatedSubmitButton').attr('disabled', 'disabled');
            var address = {
                'orgName': $.trim($('#txtOrgName').val()),
                'houseName': $.trim($('#txtHouseName').val()),
                'houseNumber': $.trim($('#txtHouseNumber').val()),
                'streetName': $.trim($('#txtStreet').val()),
                'city': $.trim($('#txtCity').val()),
                'county': $.trim($('#txtCounty').val()),
                'postCode': $.trim($('#txtManualPostCode').val())
            };
            address.formattedAddress = getFormattedManualAddress(address);
            var selectField = $("#addressSelectDropdown");
            if(address.formattedAddress.length > 0) {
                // clear selected item
                selectField.find("option").prop("selected", false);
                // remove any previously entered manual address
                selectField.find("option.manual").remove();
                var encodedAddress = encode(address);
                // insert ly entered address before "Can't find address?" entry
                var Cant_find = selectField.find('option[value="Cant_find"]');
                var string = '<option value=' + encodedAddress + ' selected="selected"  class="manual">' + address.formattedAddress + '</option>';
                Cant_find.before(string);
                // delete udprn field as this is not used for manual addresses
                $('#udprn').val("");
                $('#formattedAddress').val(address.formattedAddress);
                // show the list
                $('#box_selectAddress').show();
                $('#modalAddress').modal('hide');
            } else {
                selectField.find('option[value="selectAddress"]').prop("selected", true);
                selectField.find('option.manual').remove();
            }
        }
    });
    $("#txtPostcode").keyup(function() {
        $("#txtPostcode").val($("#txtPostcode").val().toUpperCase());
        $("#txtManualPostCode").val($("#txtPostcode").val().toUpperCase());
        addressChanged=true;
        resetAddressFields();
        clearAddressesList();
    });
});

function clearAddressesList() {
    var addressSelectDropdown = $('#addressSelectDropdown');
    addressSelectDropdown.find('option.generated').remove();
    addressSelectDropdown.find('option.manual').remove();
    addressSelectDropdown.attr('disabled', 'disabled');
}

function lookupAddresses() {
    var addressSelectDropdown = $('#addressSelectDropdown');
    $.getJSON("/payment/lookupAddresses", {
        postCode: $.trim($('#txtPostcode').val()),
        houseNumber: ""
    }, function(data) {
        var cantFind = addressSelectDropdown.find('option[value="Cant_find"]');
        clearAddressesList();
        resetAddressFields();
        for (var i = 0; i < data.length; i++) {
            var address = data[i];
            var encodedAddress = encode(address);
            var string = '<option value=' + encodedAddress + ' class="generated">' + address.formattedAddress + '</option>';
            cantFind.before(string);
        }
        addressSelectDropdown.removeAttr('disabled');
    })
    .fail(function(jqXHR, status, error) {
        $('#modalAddress').modal();
        addressSelectDropdown.removeAttr('disabled');
    });
}

function updateAddressFields(address) {
    $('#txtHouseName').val(address.houseName);
    $('#txtHouseNumber').val(address.houseNumber);
    $('#txtStreet').val(address.streetName);
    $('#txtCity').val(address.city);
    $('#txtCounty').val(address.county);
    $('#txtUdprn').val(address.udprn);
    $('#txtFormattedAddress').val(address.formattedAddress);
    $('#txtOrgName').val(address.orgName);
}

function resetAddressFields() {
    var address = {
        'orgName' : "",
        'houseName' : "",
        'houseNumber' : "",
        'streetName' : "",
        'city' : "",
        'county' : "",
        'postCode' : ""
    };
    updateAddressFields(address);
}

function getFormattedManualAddress(address) {
    var formattedAddress = "";
    if (address.orgName.length > 0) {
        formattedAddress += address.orgName + ', ';
    }
    if (address.houseName.length > 0) {
        formattedAddress += address.houseName + ', ';
    }
    if (address.houseNumber.length > 0) {
        formattedAddress += address.houseNumber + ', ';
    }
    if (address.streetName.length > 0) {
        formattedAddress += address.streetName + ', ';
    }
    if (address.city.length > 0) {
        formattedAddress += address.city;
    }
    return formattedAddress;
}

function encode(value) {
    var encoded = "";
    $.each(value, function(fieldName, fieldValue) {
        if (encoded.length !== 0) {
            encoded += "&";
        }
        encoded += fieldName;
        encoded += "=";
        encoded += encodeURIComponent(fieldValue);
    });
    return encoded;
}

function decode(value) {
    var decoded = {};
    var pairs = value.split("&");
    for (var i = 0; i < pairs.length; i++) {
        var pair = pairs[i].split("=");
        decoded[pair[0]] = decodeURIComponent(pair[1]);
    }
    return decoded;
};