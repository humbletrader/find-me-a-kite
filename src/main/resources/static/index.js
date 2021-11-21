
$(document).ready(function() {
    console.log( "document loaded" );
});

$(window).on( "load", function() {
    console.log( "window loaded" );
});

const criteria = ["none", "brand", "name", "version", "year", "size"];
var criteriaCount = 0;
var criteriaDivIdPrefix = "criteriaRow";
var criteriaValueIdPrefix = "criteria";
var criteriaNameIdPrefix = "criteriaValue";

function displayNewCriteriaRow(){
    //div
    var newSearchCriteriaRow = $("<div>").attr({id : criteriaDivIdPrefix + criteriaCount}).appendTo("#searchCriteria");

    //select
    var selectHtmlForCriteria = $("<select onchange='populateValues(\""+criteriaCount+"\")'>")
        .attr("id",  criteriaNameIdPrefix+criteriaCount)
        .appendTo(newSearchCriteriaRow);
    criteria.forEach(item => {
            $("<option>").text(item).appendTo(selectHtmlForCriteria);
    });

    //select with values
    var selectHtmlForValues = $("<select id=\""+criteriaValueIdPrefix+criteriaCount+"\">").appendTo(newSearchCriteriaRow);
    $("<option>").val("none").text("none").appendTo(selectHtmlForValues);


    //delete button
    $("<input onclick='deleteCriteriaBelow("+criteriaCount+")'>").attr({
        type : 'button',
        id : 'delete'+criteriaCount,
        value : 'Del'
     }).appendTo(newSearchCriteriaRow)

    criteriaCount = criteriaCount + 1;
}

function deleteCriteriaBelow(divCount){
    for(i=divCount; i < criteriaCount; i++){
        $("#"+criteriaDivIdPrefix+i).remove();
    }
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
            searchResultArray.forEach(item => {
                const resultLineTr = $("<tr>");

                const linkTd = $("<td>")
                linkTd.append($("<a>").text(item.brandNameVersion).attr("href", item.link));
                resultLineTr.append(linkTd)

                $("<td>").appendTo(resultLineTr)
                $("<td>").appendTo(resultLineTr)

                $("#searchResults").append(resultLineTr);
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

