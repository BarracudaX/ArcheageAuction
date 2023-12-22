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
    const errorElement = document.createElement("div")
    errorElement.className = "alert alert-danger alert-dismissible fade show mt-1"
    errorElement.innerHTML = `
        ${error}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `
    document.getElementById("errors").appendChild(errorElement)
}

function clearChildrenUntil(parent,stopChild){
    while(parent.firstChild !== stopChild){
        parent.removeChild(parent.firstChild)
    }
}

async function handleResponse(response) {
    if (response.status === 200) {
        return await response.json()
    } else{
        throw Error(await response.text())
    }
}