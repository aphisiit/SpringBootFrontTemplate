let modal;

$(document).ready(function () {

    try {
        loadData();
    }catch (e) {
        console.error(e);
    }

    $('#buttonSearchFirstName').click(function () {
        findByFirstNameIgnoreCaseContaining();
    });

    modal = new Custombox.modal({
        content: {
            effect: 'fadein',
            target: '#demo-modal',
            opacity : 1,
            speedOut : 500,
            speedIn : 500
        }
    });
});

function loadData(){

    $.ajax({
        type : 'GET',
        url : 'http://localhost:8080/SPT/users/findAllUsers',
        headers: {
            Accept: 'application/json'
        },
        contentType: "application/json; charset=UTF-8",
        async : false,
        complete : function(xhr){
            if(xhr.readyState == 4) {
                if(xhr.status == 200) {
                    let json = xhr.responseText;
                    json = JSON.parse(json);

                    if(json.length > 0){
                        $('#bodyTable').empty();
                        for(let i = 0 ; i < json.length; i++){
                            $('#bodyTable').append(
                                '<tr>' +
                                    '<td>' + (i + 1) + '</td>'+
                                    '<td>' + json[i].firstName + '</td>'+
                                    '<td>' + json[i].lastName + '</td>'+
                                    '<td>' + json[i].age + '</td>'+
                                '</tr>'
                            );
                        }
                    }else{
                        modal.open();
                        $('#bodyTable').empty().append(
                            '<tr>' +
                                '<td colspan="4" style="text-align: center;">' + $MESSAGE_NO_DATA + '</td>'+
                            '</tr>'
                        );

                        window.setTimeout(function () {
                            // Custombox.modal.close()
                            $('#exampleModal').modal('show');
                        },3000)
                    }
                }
            }
        }
    })
}

function findByFirstNameIgnoreCaseContaining() {

    let firstName = $('#inputSearchFirstName').val()

    $.ajax({
        type : 'GET',
        url : 'http://localhost:8080/SPT/users/findByFirstName?firstName=' + firstName,
        headers: {
            Accept: 'application/json'
        },
        contentType: "application/json; charset=UTF-8",
        async : false,
        complete : function(xhr){
            if(xhr.readyState == 4) {
                if(xhr.status == 200) {
                    let json = xhr.responseText;
                    json = JSON.parse(json);

                    if(json.length > 0){
                        $('#bodyTable').empty();
                        for(let i = 0 ; i < json.length; i++){
                            $('#bodyTable').append(
                                '<tr>' +
                                    '<td>' + (i + 1) + '</td>'+
                                    '<td>' + json[i].firstName + '</td>'+
                                    '<td>' + json[i].lastName + '</td>'+
                                    '<td>' + json[i].age + '</td>'+
                                '</tr>'
                            );
                        }
                    }else{
                        // modal.open();
                        // $('#exampleModal').modal('show');
                        swal ({
                            title: $LABEL_ALERT,
                            text : $MESSAGE_NO_DATA,
                            icon : "warning",
                            buttons : false,
                            dangerMode : true
                        })

                        $('#bodyTable').empty().append(
                            '<tr>' +
                                '<td colspan="4" style="text-align: center;">' + $MESSAGE_NO_DATA + '</td>'+
                            '</tr>'
                        );

                        window.setTimeout(function () {
                            // Custombox.modal.close()
                            // $('#exampleModal').modal('hide');
                            swal.close();
                        },3000)
                    }
                }
            }
        }
    })
}