let selectedDepartureLocation
let selectedDestinationLocation
let packs
const percentageSelectIDPrefix = "percentage_select_"

$(document).ready(function () {
    $.fn.dataTable.ext.errMode = 'throw';
    refreshLocations()
    fetchCategories()
    packs = $('#packs').DataTable({
        language: { url : `/resource/dataTables/${locale}.json` },
        rowId: function(data){return `pack_${data.id}`},
        createdRow: function(row, data, dataIndex) {$(row).addClass('pack');},
        serverSide: true,
        searching: false,
        columns: [
            {
                className: 'dt-control',
                orderable: false,
                data: null,
                defaultContent: ''
            },
            {
                data: "name",
                orderable: false
            },
            {
                data: "creationLocation",
                orderable: false
            },
            {
                data: "destinationLocation",
                orderable: false
            },
            {
                data: "sellPrice",
                render: formatPrice,
                orderable: false
            },
            {
                name : "profit",
                data: "profit",
                render: formatPrice
            },
            {
                name : "profitPerWorkingPoint",
                data: "workingPointsProfit",
                render: formatPrice
            },
            {
                data: "percentage",
                render: formatPercentage,
                orderable: false
            }
        ],
        order : {
            name : "profit",
            dir : "desc"
        },
        ajax: {
            url: "/pack",
            data: function (params) {
                const departureLocation = document.getElementById("departure_location").value
                const destinationLocation = document.getElementById("destination_location").value
                let continent = document.getElementById("continent").value
                let categories = Array()
                document.querySelectorAll(".category:checked").forEach(checkbox => {
                    categories.push(`categories=${checkbox.value}`)
                })
                params.category = categories.join("&")
                params.continent = continent
                params.departureLocation = departureLocation
                params.destinationLocation = destinationLocation
                params.categories = categories
            }
        }
    })

    packs.on('click', 'td.dt-control', function (e) {
        let tr = e.target.closest('tr');
        let row = packs.row(tr);

        if (row.child.isShown()) {
            row.child.hide();
        } else {
            row.child(format(row.data())).show();
        }

        function format(data){
            const packDetails = document.createElement("div")
            const recipeDetails = document.createElement("table")
            const materialDetails = document.createElement("table")
            const materialDetailsBody = document.createElement("tbody")
            packDetails.appendChild(recipeDetails)
            packDetails.appendChild(materialDetails)
            recipeDetails.id = `recipe_details_${data.id}`
            materialDetails.id = `material_details_${data.id}`
            recipeDetails.className = "table"
            recipeDetails.style.width = "100%"
            materialDetails.className = "table"
            materialDetails.style.width = "100%"
            recipeDetails.innerHTML = `
                <thead>
                    <tr>
                        <th>${producedQuantityLabel}</th>                    
                        <th>${recipeCostLabel}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th>${data.producedQuantity}</th>
                        <th>${formatPrice(data.cost)}</th>
                    </tr>
                </tbody>
            `
            materialDetails.innerHTML=`
                <thead>
                    <tr>
                        <th>${materialNameLabel}</th>
                        <th>${materialQuantityLabel}</th>
                        <th>${materialPriceLabel}</th>
                        <th>${materialTotalPriceLabel}</th>
                    </tr>
                </thead>
`

            for (let j = 0; j < data.materials.length; j++) {
                const material = data.materials[j]
                const materialRow = document.createElement("tr")
                materialRow.setAttribute("data-item-id",`${material.itemDTO.id}`)
                if (material.itemDTO.price != null) {
                    materialRow.innerHTML = `
                    <th>${material.itemDTO.name}</th>
                    <th>${material.quantity}</th>
                    <th>${formatPrice(material.itemDTO.price)}</th>
                    <th>${formatPrice(material.total)}</th>
                `
                } else {
                    materialRow.innerHTML = `
                    <th>${material.itemDTO.name}</th>
                    <th>${material.quantity}</th>
                    <th>-</th>
                    <th>-</th>
                `
                }
                materialDetailsBody.appendChild(materialRow)
            }
            materialDetails.appendChild(materialDetailsBody)
            $(`#recipe_details_${data.id}`).DataTable()
            $(`material_details_${data.id}`).DataTable()
            return packDetails
        }
    })
})

function formatPercentage(data,type,row) {
    const select = document.createElement("select")
    select.className = "form-select"
    select.disabled = !row.isUserData

    select.id=`${percentageSelectIDPrefix}${row.id}`
    select.addEventListener("change",function(){ updatePackPercentage(row.id) })

    for(let i = 80; i <= 130; i+= 10){
        const option = document.createElement("option")
        option.value = `${i}`
        option.text = `${i}%`
        if(data === i){
            option.selected = true
        }
        select.appendChild(option)
    }
    return select
}

function refreshLocations(destinationLocation, departureLocation) {
    let continent = document.getElementById("continent").value
    if (departureLocation !== undefined && destinationLocation !== undefined){
        fetch(`/location?continent=${continent}&destinationLocation=${destinationLocation}&departureLocation=${departureLocation}`)
            .then(response => handleResponse(response))
            .then(locations => handleLocations(locations,departureLocation,destinationLocation))
            .catch(reason => addError(reason.message))
    }else if(destinationLocation !== undefined){
        fetch(`/location?continent=${continent}&destinationLocation=${destinationLocation}`)
            .then(response => handleResponse(response))
            .then(locations => handleLocations(locations,departureLocation,destinationLocation))
            .catch(reason => addError(reason.message))
    }else if(departureLocation !== undefined){
        fetch(`/location?continent=${continent}&departureLocation=${departureLocation}`)
            .then(response => handleResponse(response))
            .then(locations => handleLocations(locations,departureLocation,destinationLocation))
            .catch(reason => addError(reason.message))
    }else{
        fetch(`/location?continent=${continent}`)
            .then(response => handleResponse(response))
            .then(locations => handleLocations(locations,departureLocation,destinationLocation))
            .catch(reason => addError(reason.message))
    }
}

function handleLocations(locations,departureLocation,destinationLocation) {
    const departureLocations = document.getElementById("departure_location")
    const destinationLocations = document.getElementById("destination_location")
    departureLocations.innerHTML = ""
    destinationLocations.innerHTML = ""
    const departureLocationsFragment = document.createDocumentFragment()
    const destinationLocationsFragment = document.createDocumentFragment()

    const allDepartureLocationsOption = document.createElement("option")
    allDepartureLocationsOption.value = "all"
    allDepartureLocationsOption.text = allLocations
    departureLocationsFragment.appendChild(allDepartureLocationsOption)

    const allDestinationLocationsOption = document.createElement("option")
    allDestinationLocationsOption.value = "all"
    allDestinationLocationsOption.text = allFactories
    allDestinationLocationsOption.selected = true
    destinationLocationsFragment.appendChild(allDestinationLocationsOption)

    for (let i = 0; i < locations.continentLocations.length; i++) {
        const location = locations.continentLocations[i]
        const option = document.createElement("option")
        if(departureLocation === location.id){
            option.selected = true
        }
        option.value = location.id
        option.text = capitalizeStr(location.name)
        departureLocationsFragment.appendChild(option)
    }
    for (let i = 0; i < locations.continentFactories.length; i++) {
        const location = locations.continentFactories[i]
        const option = document.createElement("option")
        if(destinationLocation === location.id){
            option.selected = true
        }
        option.value = location.id
        option.text = capitalizeStr(location.name)
        destinationLocationsFragment.appendChild(option)
    }

    departureLocations.appendChild(departureLocationsFragment)
    destinationLocations.appendChild(destinationLocationsFragment)

    if(departureLocation === undefined){
        allDepartureLocationsOption.selected = true
    }else{
        selectedDepartureLocation = departureLocation
    }

    if(destinationLocation === undefined){
        allDestinationLocationsOption.selected = true
    }else{
        selectedDestinationLocation = destinationLocation
    }
}

function changeContinent() {
    currentPage = 0
    refreshLocations()
    packs.ajax.reload()
}

function changeDepartureLocation() {
    let value = document.getElementById("departure_location").value
    if(value === "all"){
        selectedDepartureLocation = undefined
        value = undefined
    }else{
        value = parseInt(value)
    }
    currentPage = 0
    refreshLocations(selectedDestinationLocation,value)
    packs.ajax.reload()
}

function changeDestinationLocation() {
    let value = document.getElementById("destination_location").value
    if(value === "all"){
        selectedDestinationLocation = undefined
        value = undefined
    }else{
        value = parseInt(value)
    }
    currentPage = 0
    refreshLocations(value,selectedDepartureLocation)
    packs.ajax.reload()
}

async function updatePackPercentage(packID) {
    const select = document.getElementById(`${percentageSelectIDPrefix}${packID}`)
    const data = {
        packID: packID,
        percentage: select.value
    }
    const headers = createCsrfHeaders()
    headers.append("Content-Type","application/json")
    await fetch("/pack/percentage", {method: "PUT", body: JSON.stringify(data), headers : headers,credentials : "same-origin"})
        .then(async response => {
            if (response.status !== 200) {
                addError(await response.text())
            } else {
                packs.ajax.reload()
            }
        })
}