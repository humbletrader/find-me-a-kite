
$(document).ready(function() {
    console.log( "document loaded" );
});

$(window).on( "load", function() {
    console.log( "window loaded" );
});

const criteria = ["none", "brand", "name", "version", "year", "size"]
var criteriaCount = 0;
var criteriaValueIdPrefix = "criteria"
var criteriaNameIdPrefix = "criteriaValue"

function displayNewCriteriaRow(){

    var newSearchCriteriaRow = $("<div>").appendTo("#searchCriteria");
    var selectHtmlForCriteria = $("<select onchange='populateValues(\""+criteriaCount+"\")'>")
        .attr("id",  criteriaNameIdPrefix+criteriaCount)
        .appendTo(newSearchCriteriaRow);

    criteria.forEach(item => {
            $("<option>").text(item).appendTo(selectHtmlForCriteria);
    });

    var selectHtmlForValues = $("<select id=\""+criteriaValueIdPrefix+criteriaCount+"\">").appendTo(newSearchCriteriaRow);
    $("<option>").val("none").text("none").appendTo(selectHtmlForValues);
    criteriaCount = criteriaCount + 1;
}

function collectCriteriaValues(divCount){
    var criteria = { "category" : $("#category").val()}
    for(i=0; i < divCount; i++){
        var selectedCriteria = $("#"+criteriaNameIdPrefix + i).val();
        var selectedCriteriaValue = $("#"+criteriaValueIdPrefix + i).val();
        criteria[selectedCriteria] = selectedCriteriaValue;
    }
    return criteria;
}

function populateValues(divCount){

    var currentCriteria = $("#"+criteriaNameIdPrefix + divCount).val();
    var result = { "target" : currentCriteria}
    result["criteria"] = collectCriteriaValues(divCount);

    console.log("populate values for " + JSON.stringify(result));

     $.ajax({
            url: "/searchDistinctValues",
            contentType : "application/json",
            data: JSON.stringify(result),
            type: "POST",
            dataType : "json",
         })
         .done(function( distinctValues ) {
             console.log(distinctValues);
             distinctValues.forEach(item => {
                $("<option>").text(item).appendTo("#"+criteriaValueIdPrefix+divCount);
             });
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

function find(){
    $("#searchResults div").empty();

   var postData = {
    "page" : 0,
    "criteria" : collectCriteriaValues(criteriaCount)
   }
   console.log("sending to server..."+JSON.stringify(postData));


    $.ajax({
            url: "/searchv2",
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

