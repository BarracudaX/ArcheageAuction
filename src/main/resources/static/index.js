
const errors = new Set()

function changeLocale(language){
    let newURL = new URL(window.location.href)
    newURL.searchParams.set("locale",language)

    window.location.replace(newURL)
}

function capitalizeStr(input){
    return input.toLowerCase().replace(/\b\w/g,function(match){
        return match.toUpperCase()
    })
}

function addError(error){
    if (errors.has(error)){
        return
    }
    errors.add(error)
    const errorElement = document.createElement("div")
    errorElement.className = "alert alert-danger alert-dismissible fade show mt-1"
    errorElement.innerHTML = `
        ${error}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `
    document.getElementById("messages").appendChild(errorElement)
}

function addSuccess(success){
    const successElement = document.createElement("div")
    successElement.className = "alert alert-success alert-dismissible fade show mt-1"
    successElement.innerHTML = `
        ${success}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `
    document.getElementById("messages").appendChild(successElement)
}

function clearChildrenUntil(parent,stopChild){
    while(parent.firstChild !== stopChild){
        parent.removeChild(parent.firstChild)
    }
}

async function handleResponse(response) {
    if (response.status === 200) {
        if(response.headers.get("Content-Type") === "application/json"){
            return await response.json()
        }else{
            return await response.text()
        }
    } else{
        throw Error(await response.text())
    }
}

function createCsrfHeaders() {
    const csrfToken = document.querySelector("meta[name='_csrf']").content
    const csrfHeaderName = document.querySelector("meta[name='_csrf_header']").content
    const headers = new Headers()
    headers.append(csrfHeaderName,csrfToken)

    return headers
}

function formatPrice(data){
    return `${data.gold} ${goldLabel} ${data.silver} ${silverLabel} ${data.copper} ${copperLabel}`
}