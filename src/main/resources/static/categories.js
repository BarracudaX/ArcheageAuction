function fetchCategories(){
    fetch("/category")
        .then(response => handleResponse(response))
        .then(categories => { handleCategories(categories,document.getElementById("categories")) })
}

function handleCategories(categories,container,parentCategory){
    for(let i = 0 ; i < categories.length; i++){
        const category = categories[i]
        const categoryFragment = document.createDocumentFragment()
        const categoryFlexbox = document.createElement("div")
        const categoryDataFlexbox = document.createElement("div")
        const categoryExpandButton = document.createElement("button")
        const categoryCheckbox = document.createElement("input")
        const categoryLabel = document.createElement("label")
        categoryFlexbox.appendChild(categoryDataFlexbox)
        categoryFragment.appendChild(categoryFlexbox)

        categoryFlexbox.className = "d-flex flex-column"
        categoryDataFlexbox.className = "d-flex align-items-center"
        categoryDataFlexbox.style.columnGap = "5px"

        categoryCheckbox.type = "checkbox"
        categoryCheckbox.className = `form-check-input category`
        categoryCheckbox.value = category.id
        categoryCheckbox.addEventListener("change",(event) => { onCategoryChange(event) })
        if(parentCategory !== undefined){
            categoryCheckbox.classList.add(`category${parentCategory.id}`)
            categoryCheckbox.setAttribute("data-parent-category",`category${parentCategory.id}`)
        }
        categoryCheckbox.id = `category${category.id}`
        categoryLabel.className = "form-check-label flex-fill"
        categoryLabel.htmlFor = categoryCheckbox.id
        categoryLabel.textContent = category.name
        categoryExpandButton.textContent = "+"
        categoryExpandButton.className = "plus"

        categoryDataFlexbox.appendChild(categoryExpandButton)
        categoryDataFlexbox.appendChild(categoryCheckbox)
        categoryDataFlexbox.appendChild(categoryLabel)
        if(category.subcategories !== undefined && category.subcategories.length !== 0){
            const subcategories = document.createElement("div")
            subcategories.className = "collapse ms-3 mt-1"
            subcategories.id = `subcategories${category.id}`
            categoryFlexbox.appendChild(subcategories)
            categoryExpandButton.setAttribute("data-bs-toggle","collapse")
            categoryExpandButton.setAttribute("aria-expanded","false")
            categoryExpandButton.setAttribute("aria-controls",subcategories.id)
            categoryExpandButton.setAttribute("data-bs-target",`#${subcategories.id}`)
            categoryExpandButton.onclick = (event) => { onCategoryBtn(event) }
            handleCategories(category.subcategories,subcategories,category)
        }else{
            categoryExpandButton.style.visibility = "hidden"
        }
        container.appendChild(categoryFragment)
    }

    function onCategoryChange(event){
        const categories = document.getElementsByClassName(event.currentTarget.id)

        for(let category of categories){
            category.checked = event.currentTarget.checked
        }
        const parent = event.currentTarget.getAttribute("data-parent-category")
        if(parent !== null && document.querySelectorAll(`.${event.currentTarget.getAttribute("data-parent-category")}:checked`).length === 0 ){
            document.getElementById(parent).checked = false
        }

        packs.ajax.reload()
    }

    function onCategoryBtn(event){
        if(event.target.textContent === "+"){
            event.target.textContent = "-"
            event.target.className = "minus"
        }else{
            event.target.textContent = "+"
            event.target.className = "plus"
        }
    }
}