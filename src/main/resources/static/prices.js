$(document).ready(function(){
    $.fn.dataTable.ext.errMode = function ( settings, helpPage, message ) {
        addError(message.substring(38))
    };

    $("#prices").DataTable({
        language: { url : `/resource/dataTables/${locale}.json` },
        rowId: function(data){return `item_${data.id}`},
        createdRow: function(row, data, dataIndex) {$(row).addClass('price');},
        serverSide: true,
        searching: false,
        columns:[
            {
                data : "name",
                orderable: false,
            },
            {
                data : "price.gold",
                orderable: false,
                render : function (data, type, row, meta) {
                    return `<input type="number" value='${data}' id="${row.id}_gold" min="0" />`
                }
            },
            {
                data : "price.silver",
                orderable: false,
                render : function (data, type, row, meta) {
                    return `<input type="number" value='${data}' id="${row.id}_silver" min="0" max="99"/>`
                }
            },
            {
                data : "price.copper",
                orderable: false,
                render : function (data, type, row, meta) {
                    return `<input type="number" value='${data}' id="${row.id}_copper" min="0" max="99" />`
                }
            },
            {
                data: null,
                orderable: false,
                render : function(data,type,row,meta){
                    const saveBtn = document.createElement("button")
                    saveBtn.className = "btn btn-primary w-100"
                    saveBtn.onclick = () => { savePrice(data.id) }
                    saveBtn.textContent = saveBtnLabel
                    return saveBtn
                }
            }
        ],
        ajax: {
            url : "/user/price"
        }
    })
})

function savePrice(itemID){
    let gold = document.getElementById(`${itemID}_gold`).value
    let silver = document.getElementById(`${itemID}_silver`).value
    let copper = document.getElementById(`${itemID}_copper`).value


    if(gold === ""){
        gold = 0
        document.getElementById(`${itemID}_gold`).value=0
    }

    if(silver === ""){
        silver = 0
        document.getElementById(`${itemID}_silver`).value=0
    }

    if(copper === ""){
        copper = 0
        document.getElementById(`${itemID}_copper`).value=0
    }

    const headers = createCsrfHeaders()
    headers.append("Content-Type","application/json")
    const request = {
        itemID: itemID,
        price:{
            gold: gold,
            silver: silver,
            copper: copper
        }
    }
    fetch("/user/price",{method : "POST",body : JSON.stringify(request),credentials : "same-origin",headers : headers})
        .then((response) => handleResponse(response))
        .then(response => addSuccess(response))
        .catch(error => { addError(error.message) })
}