function changeLocale(language){
    let newURL = new URL(window.location.href)
    newURL.searchParams.set("locale",language)

    window.location.replace(newURL)
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