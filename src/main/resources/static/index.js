function changeLocale(language){
    let newURL = new URL(window.location.href)
    newURL.searchParams.set("locale",language)

    window.location.replace(newURL)
}