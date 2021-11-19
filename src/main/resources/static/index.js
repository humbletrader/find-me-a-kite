$( document ).ready(function() {
    console.log( "document loaded" );
});

$( window ).on( "load", function() {
    console.log( "window loaded" );
});

function populateSize(){
    const selectedCategory = $("#category").val();
    const selectedBrand = $("#brand").val();
    const selectedName = $("#name").val();
    const selectedVersion = $("#version").val();

     $.ajax({
        url: "/sizeForVersionNameBrandAndCategory",
        contentType : "application/json",
        data: {"category": selectedCategory, "brand": selectedBrand, "name" : selectedName, "version": selectedVersion},
        type: "GET",
        dataType : "json",
    })
    .done(function( sizes ) {
        console.log(sizes);
        sizes.forEach(addSizeToHtmlSelect);
    })
    .fail(function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
    });
}

function addSizeToHtmlSelect(item, index, array){
     $("<option>").text(item).appendTo("#size");
}

function populateVersion(){
    $("#version").find('option').remove().end().append('<option value="none">none</option>').val('none');

    const selectedCategory = $("#category").val();
    const selectedBrand = $("#brand").val();
    const selectedName = $("#name").val();

    $.ajax({
        url: "/versionsForNameBrandAndCategory",
        contentType : "application/json",
        data: {"category": selectedCategory, "brand": selectedBrand, "name" : selectedName},
        type: "GET",
        dataType : "json",
    })
    .done(function( versions ) {
        console.log(versions);
        versions.forEach(addVersionsToHtmlSelect);
    })
    .fail(function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
    });
}

function addVersionsToHtmlSelect(item, index, array){
    $("<option>").text(item).appendTo("#version");
}

function populateProductNames(){
    $("#name").find('option').remove().end().append('<option value="none">none</option>').val('none');
    $("#version").find('option').remove().end().append('<option value="none">none</option>').val('none');
    $("#size").find('option').remove().end().append('<option value="none">none</option>').val('none');

    const selectedCategory = $("#category").val();
    const selectedBrand = $("#brand").val();

    $.ajax({
        url: "/namesForCategoryAndBrand",
        contentType : "application/json",
        data: {"category": selectedCategory, "brand": selectedBrand},
        type: "GET",
        dataType : "json",
    })
    .done(function( brands ) {
        console.log(brands);
        brands.forEach(addNameToHtmlSelect);
    })
    .fail(function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
    });
}

function addNameToHtmlSelect(item, index, array){
    $("<option>").text(item).appendTo("#name");
}

function populateBrands(){
    $("#brand").find('option').remove().end().append('<option value="none">none</option>').val('none');
    $("#name").find('option').remove().end().append('<option value="none">none</option>').val('none');
    $("#version").find('option').remove().end().append('<option value="none">none</option>').val('none');
    $("#size").find('option').remove().end().append('<option value="none">none</option>').val('none');

    const selectedCategory = $("#category").val();

    $.ajax({
        url: "/brandsForCategory",
        contentType : "application/json",
        data: {"category": selectedCategory},
        type: "GET",
        dataType : "json",
    })
    .done(function( brands ) {
        console.log(brands);
        brands.forEach(addBrandToHtmlSelect);
    })
    .fail(function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
    });
}

function addBrandToHtmlSelect(item, index, array){
    $("<option>").text(item).appendTo("#brand");
}

function find(){
    $("#searchResults div").empty();

    const selectedCategory = $("#category").val();
    const selectedBrand = $("#brand").val();
    const selectedName = $("#name").val();
    const selectedVersion = $("#version").val();

    const postData = {
        "category": selectedCategory,
        "brand": selectedBrand,
        "productName": selectedName,
        "productVersion": selectedVersion,
        "size": "12",
        "color" : "yellow",
        "page": 0
    }
    console.log("sending to server...");
    console.log(postData);

    $.ajax({
            url: "/search",
            contentType : "application/json",
            data: JSON.stringify(postData),
            type: "POST",
            dataType : "json",
     })
     .done(function( searchResultArray ) {
            console.log(searchResultArray);
            searchResultArray.forEach(addSearchResultItem);
     })
     .fail(function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
     })
     .always(function( xhr, status ) {
        console.log( "The request is complete!" );
     });
}

function addSearchResultItem(item, index, arr) {
     console.log(item);
     const resultLineDiv = $("<div>");
     $("#searchResults").append(resultLineDiv);
     resultLineDiv.append($("<a>").text(item.brandNameVersion).attr("href", item.link));
}
