function changeContinent(){
    let continent = document.getElementById("continent").value
    let url = new URL(window.location.href)
    url.search=''
    url.searchParams.set("continent",continent)

    window.location.href = url.toString()
}

function changeDepartureLocation(){
    let departureLocation = document.getElementById("departure_location").value
    let url = new URL(window.location.href)

    if(departureLocation === "all"){
        url.searchParams.delete("departureLocation")
    }else{
        url.searchParams.set("departureLocation",departureLocation)
    }

    window.location.href = url.toString()
}

function changeDestinationLocation(){
    let destinationLocation = document.getElementById("destination_location").value
    let url = new URL(window.location.href)

    if(destinationLocation === "all"){
        url.searchParams.delete("destinationLocation")
    }else{
        url.searchParams.set("destinationLocation",destinationLocation)
    }

    window.location.href = url.toString()
}