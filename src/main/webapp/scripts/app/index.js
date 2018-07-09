let modal;

$(document).ready(function () {

    try {
        findByFirstNameIgnoreCaseContaining();
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


function findByFirstNameIgnoreCaseContaining() {

    let firstName = $('#inputSearchFirstName').val()
    $('.lds-ring').show();
    window.setTimeout(function () {
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
                                    '<td>' +
                                    '<button type="button" class="btn btn-primary" onclick="editData(' + json[i].id + ')">' + $BUTTON_EDIT + '</button>&nbsp;' +
                                    '<button type="button" class="btn btn-danger" onclick="deleteData(' + json[i].id + ')">' + $BUTTON_DELETE + '</button>' +
                                    '</td>'+
                                    '</tr>'
                                );
                            }

                            $('.lds-ring').hide();
                        }else{
                            // modal.open();
                            // $('#exampleModal').modal('show');
                            $('.lds-ring').hide();
                            swal ({
                                title: $LABEL_ALERT,
                                text : $MESSAGE_NO_DATA,
                                icon : "warning",
                                buttons : false,
                                dangerMode : true
                            })

                            $('#bodyTable').empty().append(
                                '<tr>' +
                                '<td colspan="5" style="text-align: center;">' + $MESSAGE_NO_DATA + '</td>'+
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
    },100);
}

function deleteData(id) {

    swal("A wild Pikachu appeared! What do you want to do?", {
        buttons: {
            cancel: "Cancel",
            catch: {
                text: "DELETE",
                value: true,
            },
        },
    }).then((value) => {
        switch (value) {

            case true :
                $.ajax({
                    type : 'POST',
                    url : 'http://localhost:8080/SPT/users/deleteById/' + id,
                    headers: {
                        Accept: 'application/json'
                    },
                    contentType: "application/json; charset=UTF-8",
                    async : false,
                    complete : function(xhr){
                        if(xhr.readyState == 4) {
                            if(xhr.status == 200) {
                                swal ({
                                    title: $LABEL_ALERT,
                                    text : $MESSAGE_DELETE_SUCCESS,
                                    icon : "success",
                                    buttons : false,
                                    dangerMode : false
                                })
                                window.setTimeout(function () {
                                    swal.close();
                                    window.setTimeout(function () {
                                        findByFirstNameIgnoreCaseContaining();
                                    },500)
                                },3000);
                            }
                        }
                    }
                })
                break;

            default:
                swal("Canceled",$MESSAGE_DELETE_CANCELED,"error");
                window.setTimeout(function () {
                    swal.close();
                },3000);


        }
    });

}

function editData(id) {
    $('.lds-ring').show();

    window.setTimeout(function () {
        $.ajax({
            type : 'POST',
            url : 'http://localhost:8080/SPT/users/findById/' + id,
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

                        console.log(json);
                        // if(json.length > 0){

                        // }
                        $('.lds-ring').hide();
                    }else {
                        $('.lds-ring').hide();
                    }
                }else {
                    $('.lds-ring').hide();
                }
            }
        })
    },100);


}