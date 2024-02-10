let currentPage = 0
let pageSize = 10
let selectedDepartureLocation
let selectedDestinationLocation
const percentageSelectIDPrefix = "percentage_select_"

document.addEventListener("DOMContentLoaded", function () {
    refreshLocations()
    refreshPacks()
})

function refreshLocations(){
    let continent = document.getElementById("continent").value
    if(selectedDepartureLocation !== undefined && selectedDestinationLocation !== undefined){
        fetch(`/location?continent=${continent}&destinationLocation=${selectedDestinationLocation}&departureLocation=${selectedDepartureLocation}`)
            .then(response => handleResponse(response))
            .then(locations => handleLocations(locations))
            .catch(reason => addError(reason.message))
    }else if(selectedDestinationLocation !== undefined){
        fetch(`/location?continent=${continent}&destinationLocation=${selectedDestinationLocation}`)
            .then(response => handleResponse(response))
            .then(locations => handleLocations(locations))
            .catch(reason => addError(reason.message))
    }else if(selectedDepartureLocation !== undefined){
        fetch(`/location?continent=${continent}&departureLocation=${selectedDepartureLocation}`)
            .then(response => handleResponse(response))
            .then(locations => handleLocations(locations))
            .catch(reason => addError(reason.message))
    }else{
        fetch(`/location?continent=${continent}`)
            .then(response => handleResponse(response))
            .then(locations => handleLocations(locations))
            .catch(reason => addError(reason.message))
    }
}

function refreshPacks(){
    fetchPage(currentPage).then()
}

function nextPage(){
    fetchPage(currentPage+1).then(() => {
        currentPage = currentPage +1
    }).catch(reason => addError(reason.message))
}

function previousPage(){
    fetchPage(currentPage-1).then(() => {
        currentPage = currentPage - 1
    }).catch(reason => addError(reason.message))
}

function fetchPage(page){
    const departureLocation = document.getElementById("departure_location").value
    const destinationLocation = document.getElementById("destination_location").value
    let continent = document.getElementById("continent").value

    if(departureLocation === "all" && destinationLocation === "all"){
        return fetch(`/pack?page=${page}&size=${pageSize}&continent=${continent}&sort=netProfit.gold,desc&sort=netProfit.silver,desc&sort=netProfit.copper,desc`)
            .then(response => handleResponse(response))
            .then(data => {
                handlePacks(data)
            }).catch(reason => addError(reason.message))
    }else if(departureLocation === "all"){
        return fetch(`/pack?page=${page}&size=${pageSize}&continent=${continent}&destinationLocation=${destinationLocation}&sort=netProfit.gold,desc&sort=netProfit.silver,desc&sort=netProfit.copper,desc`)
            .then(response => handleResponse(response))
            .then(data => {
                handlePacks(data)
            }).catch(reason => addError(reason.message))
    }else if(destinationLocation === "all"){
        return fetch(`/pack?page=${page}&size=${pageSize}&continent=${continent}&departureLocation=${departureLocation}&sort=netProfit.gold,desc&sort=netProfit.silver,desc&sort=netProfit.copper,desc`)
            .then(response => handleResponse(response))
            .then(data => {
                handlePacks(data)
            }).catch(reason => addError(reason.message))
    }else{
        return fetch(`/pack?page=${page}&size=${pageSize}&continent=${continent}&departureLocation=${departureLocation}&destinationLocation=${destinationLocation}&sort=netProfit.gold,desc&sort=netProfit.silver,desc&sort=netProfit.copper,desc`)
            .then(response => handleResponse(response))
            .then(data => {
                handlePacks(data)
            }).catch(reason => addError(reason.message))
    }
}

function handleLocations(locations) {
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
        if(selectedDepartureLocation === location.id){
            option.selected = true
        }
        option.value = location.id
        option.text = capitalizeStr(location.name)
        departureLocationsFragment.appendChild(option)
    }
    for (let i = 0; i < locations.continentFactories.length; i++) {
        const location = locations.continentFactories[i]
        const option = document.createElement("option")
        if(selectedDestinationLocation === location.id){
            option.selected = true
        }
        option.value = location.id
        option.text = capitalizeStr(location.name)
        destinationLocationsFragment.appendChild(option)
    }

    if(selectedDepartureLocation === undefined){
        allDepartureLocationsOption.selected = true
    }

    if(selectedDestinationLocation === undefined){
        allDestinationLocationsOption.selected = true
    }

    departureLocations.appendChild(departureLocationsFragment)
    destinationLocations.appendChild(destinationLocationsFragment)
}

function handlePacks(data) {
    const container = document.getElementById("pagination_container")
    const previousPacks = document.getElementById("packs")
    const fragment = document.createDocumentFragment()
    if (previousPacks != null) {
        let paginationButtons = document.getElementById("pagination_btns")
        clearChildrenUntil(container, paginationButtons)
    }

    const packs = data.packs
    const packsDiv = document.createElement("div")
    packsDiv.id = "packs"
    fragment.appendChild(packsDiv)

    document.getElementById("previous_btn").disabled = !data.hasPrevious
    document.getElementById("next_btn").disabled = !data.hasNext

    for (let i = 0; i < packs.length; i++) {
        const pack = packs[i]
        const packRow = document.createElement("div")
        const hrElement = document.createElement("hr")
        packRow.innerHTML = `
                <div class="row w-100">
                    <div class="d-flex justify-content-center col">
                        <button class="btn btn-success" type="button" data-bs-toggle="collapse" data-bs-target="#pack_${pack.id}" aria-expanded="false" aria-controls="pack_${pack.id}">+</button>
                    </div>
                    <div class="text-center col">${pack.name}</div>
                    <div class="text-center col">${pack.creationLocation}</div>
                    <div class="text-center col">${pack.destinationLocation}</div>
                    <div class="text-center col">${pack.sellPrice.gold+' '+goldLabel+' '+pack.sellPrice.silver+' '+silverLabel+' '+pack.sellPrice.copper+' '+copperLabel}</div>
                    <div class="text-center col">${pack.profit.gold+' '+goldLabel+' '+pack.profit.silver+' '+silverLabel+' '+pack.profit.copper+' '+copperLabel}</div>
                    <select class="form-select col" id="${percentageSelectIDPrefix}${pack.id}" ${data.isUserData ? '' : 'disabled'} onchange="updatePackPercentage(${pack.id})"></select>
                </div>
        `
        const insertBeforeThis = document.createElement("div")
        packRow.appendChild(insertBeforeThis)
        const materialsDiv = document.createElement("div")
        materialsDiv.className="row m-0 mt-4 collapse"
        materialsDiv.id = `pack_${pack.id}`
        materialsDiv.innerHTML = `
            <div class="row">
                <h4 class="text-center col">${producedQuantityLabel}</h4>
                <h4 class="text-center col">${recipeCostLabel}</h4>
            </div>
            <div class="row">
                <h4 class="text-center col">${pack.producedQuantity}</h4>
                <h4 class="text-center col">${pack.cost.gold+' '+goldLabel+' '+pack.cost.silver+' '+silverLabel+' '+pack.cost.copper+' '+copperLabel}</h4>
            </div>
            <div class="row">
                <h4 class="text-center col">${materialNameLabel}</h4>
                <h4 class="text-center col">${materialQuantityLabel}</h4>
                <h4 class="text-center col">${materialPriceLabel}</h4>
                <h4 class="text-center col">${materialTotalPriceLabel}</h4>
            </div>
        `

        for (let j = 0; j < pack.materials.length; j++) {
            const material = pack.materials[j]
            const materialRow = document.createElement("div")
            materialRow.className = "row"
            if (material.itemDTO.price != null) {
                materialRow.innerHTML = `
                 <div class="text-center col">${material.itemDTO.name}</div>
                 <div class="text-center col">${material.quantity}</div>
                 <div class="text-center col">${material.itemDTO.price.gold +' '+ goldLabel + ' ' + material.itemDTO.price.silver +' '+ silverLabel + ' ' + material.itemDTO.price.copper +' '+ copperLabel}</div>
                 <div class="text-center col">${material.total.gold +' '+ goldLabel + ' ' + material.total.silver +' '+ silverLabel + ' ' + material.total.copper +' '+ copperLabel}</div>
                `
            } else {
                materialRow.innerHTML = `
                 <div class="text-center col">${material.itemDTO.name}</div>
                 <div class="text-center col">${material.quantity}</div>
                 <div class="text-center col">-</div>
                 <div class="text-center col">-</div>
                `
            }
            materialsDiv.appendChild(materialRow)
        }
        packRow.appendChild(materialsDiv)
        packRow.appendChild(hrElement)
        packsDiv.appendChild(packRow)
    }
    fragment.appendChild(packsDiv)
    container.insertBefore(fragment, container.firstChild)

    for (let i = 0; i < packs.length; i++) {
        const pack = packs[i]
        const select = document.getElementById(`${percentageSelectIDPrefix}${pack.id}`)
        for(let i = 80; i <= 130; i+= 10){
            const option = document.createElement("option")
            option.value = `${i}`
            option.text = `${i}%`
            if(pack.percentage === i ){
                option.selected = true
            }
            select.appendChild(option)
        }
    }
}


function changeContinent() {
    currentPage = 0
    refreshLocations()
    refreshPacks()
}

function changeDepartureLocation() {
    const value = document.getElementById("departure_location").value
    if(value !== "all"){
        selectedDepartureLocation = parseInt(value)
    }else{
        selectedDepartureLocation = undefined
    }
    currentPage = 0
    refreshLocations()
    refreshPacks()
}

function changeDestinationLocation() {
    const value = document.getElementById("destination_location").value
    if(value !== "all"){
        selectedDestinationLocation = parseInt(value)
    }else{
        selectedDestinationLocation = undefined
    }
    currentPage = 0
    refreshLocations()
    refreshPacks()
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
                refreshPacks()
            }
        })
}