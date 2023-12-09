
let currentPage = 0
let pageSize = 20

document.addEventListener("DOMContentLoaded", function () {
    fetch(`/user/price?page=${currentPage}&size=${pageSize}`)
        .then(response => handleResponse(response))
        .then(data => { handleData(data) })
        .catch(reason => addError(reason.message))
})

async function handleResponse(response) {
    if (response.status === 200) {
        return await response.json()
    } else{
        throw Error(await response.text())
    }
}

function nextPrices(){
    fetch(`/user/price?page=${currentPage+1}&size=${pageSize}`)
        .then(response => handleResponse(response))
        .then(data => {
            currentPage = currentPage + 1
            handleData(data)
        }).catch(reason => addError(reason.message))
}

function previousPrices(){
    fetch(`/user/price?page=${currentPage - 1}&size=${pageSize}`)
        .then(response => handleResponse(response))
        .then(data => {
            currentPage = currentPage - 1
            handleData(data)
        }).catch(reason => addError(reason.message))
}

function handleData(data){
    let container = document.getElementById("user_price_list")
    container.innerHTML = ''
    let fragment = document.createDocumentFragment()
    let items = data.items.content
    let prices = data.prices
    let pricesDiv = document.createElement("div")
    let h2Label = document.createElement("h2")
    let hr = document.createElement("hr")
    h2Label.className = "text-center col m-0 p-0"
    h2Label.textContent = pricesLabel
    pricesDiv.className = "row"
    pricesDiv.appendChild(h2Label)

    fragment.appendChild(pricesDiv)
    fragment.appendChild(hr)

    document.getElementById("previous_btn").disabled = !data.hasPrevious
    document.getElementById("next_btn").disabled = !data.hasNext
    for (let i = 0; i < items.length; i++) {
        let item = document.createElement("div")
        let price = prices[`${items[i].id}`]
        item.className = "row gx-4 mt-2"
        item.innerHTML = `
                <h4 class="text-center col-4 p-0 m-0">${items[i].name}</h4>
                <div class="col-2 m-0 row ">
                    <label class="col p-0 text-center">${goldLabel}</label>
                    <input type="number" class="form-input col p-0" min="0" value="${price !== undefined && price !== null ? price.gold : ''}" />
                 </div>
                <div class="col-2 m-0 row ">
                    <label class="col p-0 text-center">${silverLabel}</label>
                    <input type="number" class="form-input col p-0" min="0" max="99" value="${price !== undefined && price !== null ? price.silver : ''}"  />
                </div>
                 <div class="col-2 m-0 row ">
                    <label class="col p-0 text-center">${copperLabel}</label>
                    <input type="number" class="form-input col p-0" min="0" max="99" value="${price !== undefined && price !== null ? price.copper : ''}" />
                 </div>
                 <button class="btn btn-primary col-2">${saveBtnLabel}</button>
                `
        fragment.appendChild(item)
    }
    container.appendChild(fragment)
}