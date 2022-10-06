
var chooseItemText = "Choose..."
var currentResultsPage = 0;
var criteria = ["brand", "name", "version", "year", "size", "condition"];
var criteriaCount = 0;
var criteriaDivIdPrefix = "criteriaRow";
var criteriaValueIdPrefix = "criteria";
var criteriaNameIdPrefix = "criteriaValue";

function disableFilterInputsForLevel(level){
    console.log("disabling input #"+criteriaNameIdPrefix+level)
    $("#"+criteriaNameIdPrefix+level).attr("disabled", "disabled");
    $("#"+criteriaValueIdPrefix+level).attr("disabled", "disabled");
}

function previousFormsHaveCorrectStatus(){
    if(criteriaCount > 0){
        var selectedCriteria = $("#"+criteriaNameIdPrefix + (criteriaCount-1)).val();
        var selectedCriteriaValue = $("#"+criteriaValueIdPrefix + (criteriaCount-1)).val();
        //console.log("selected criteria: "+selectedCriteria +" and value: "+selectedCriteriaValue);
        return selectedCriteria != "none" && selectedCriteriaValue != "none";
    }else{
        return true;
    }
}

function displayNewCriteriaRow(){

    if(!previousFormsHaveCorrectStatus()){
        alert("please fill the existing forms before adding new filters");
        return;
    }

    //disable previous criteria
    if(criteriaCount > 0) disableFilterInputsForLevel(criteriaCount - 1)

    //div
    var newSearchCriteriaRow = $("<div>")
        .attr({
            id : criteriaDivIdPrefix + criteriaCount,
            class : "input-group mb-3"
         })
        .appendTo("#searchCriteria");

    var firstColumn = $("<div class='input-group-prepend'>").appendTo(newSearchCriteriaRow)

    //select
    var selectHtmlForCriteria = $("<select onchange='populateValues(\""+criteriaCount+"\")'>")
        .attr("id",  criteriaNameIdPrefix+criteriaCount)
        .attr("class", "form-control")
        .appendTo(firstColumn);
    $("<option>").val("none").text(chooseItemText).appendTo(selectHtmlForCriteria);

    criteria.forEach(item => {
            $("<option>").val(item).text(item).appendTo(selectHtmlForCriteria);
    });

    //select with values
    var selectHtmlForValues = $("<select id=\""+criteriaValueIdPrefix+criteriaCount+"\">")
            .attr("class", "form-control")
            .attr("disabled", "disabled")
            .appendTo(newSearchCriteriaRow);
    $("<option>").val("none").text(chooseItemText).appendTo(selectHtmlForValues);


    var thirdColumn = $("<div class='input-group-append'>").appendTo(newSearchCriteriaRow)
    //delete button
    var deleteButton = $("<button onclick='deleteCriteriaBelow("+criteriaCount+")'>").attr({
        id : 'delete'+criteriaCount,
        class: "form-control"
     }).appendTo(thirdColumn)
     $("<i>").attr("class", "fas fa-trash").appendTo(deleteButton)

    criteriaCount = criteriaCount + 1;
}

function deleteCriteriaBelow(divCount){
    for(i=divCount; i < criteriaCount; i++){
        $("#"+criteriaDivIdPrefix+i).remove();
    }
}

function collectCriteriaValues(divCount){
    var criteria = {
        "category" : $("#category").val(),
        "country" : $("#country").val()
    }
    for(i=0; i < divCount; i++){
        var selectedCriteria = $("#"+criteriaNameIdPrefix + i).val();
        var selectedCriteriaValue = $("#"+criteriaValueIdPrefix + i).val();
        criteria[selectedCriteria] = selectedCriteriaValue;
    }
    return criteria;
}

function populateValues(divCount){

    //delete previous options in select
    $("#"+criteriaValueIdPrefix+divCount).empty();
    $("<option>").val("none").text(chooseItemText).appendTo("#"+criteriaValueIdPrefix+divCount);

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
                var criteriaValueHtmlSelect = $("#"+criteriaValueIdPrefix+divCount);
                criteriaValueHtmlSelect.removeAttr("disabled");
                $("<option>").text(item).appendTo(criteriaValueHtmlSelect);
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

function find(pageToFind){

    if(!previousFormsHaveCorrectStatus()){
        alert("please fill all forms before search !");
        return;
    }
    //disable last criteria
    //if(criteriaCount > 0) disableFilterInputsForLevel(criteriaCount-1)

    $("#searchResults").empty();

   var postData = {
    "page" : pageToFind,
    "criteria" : collectCriteriaValues(criteriaCount)
   }
   console.log("sending to server..."+JSON.stringify(postData));
   $.ajax({
        url: "/search",
        contentType : "application/json",
        data: JSON.stringify(postData),
        type: "POST",
        dataType : "json",
   })
   .done(function( searchResultPage ) {
        console.log(searchResultPage);
        searchResultPage.items.forEach(item => {
            const resultLineTr = $("<tr>");

            const linkTd = $("<td>")
            linkTd.append($("<a>").text(item.brandNameVersion).attr("href", item.link).attr("target", "_blank"));
            resultLineTr.append(linkTd)

            $("<td>").text(item.size).appendTo(resultLineTr)
            $("<td>").text(item.price.toFixed(2)).appendTo(resultLineTr)
            $("<td>").text(item.condition).appendTo(resultLineTr)

            $("#searchResults").append(resultLineTr);
        });

        //handle pagination
        var paginationUl = $(".pagination")
        paginationUl.empty();
        for(i=0; i < searchResultPage.currentPage; i++){
            var pageLi = $("<li>").attr("class", "page-item").appendTo(paginationUl)
            $("<a>").attr({
                "class" : "page-link",
                "onclick" : "find("+i+")"
            }).text(i+1).appendTo(pageLi)
        }
        var pageLi = $("<li>").attr("class", "page-item active").appendTo(paginationUl)
        $("<a>").attr({
            "class" : "page-link"
        }).text(searchResultPage.currentPage + 1).appendTo(pageLi)

        if(searchResultPage.nextPage){
            var pageLi = $("<li>").attr("class", "page-item").appendTo(paginationUl)
            $("<a>").attr({
                "class" : "page-link",
                "onclick" : "find("+(pageToFind+1)+")",
                }).text("Next").appendTo(pageLi)
        }
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

window.onload = function(){
    $("#newsModalDialog").modal()
}

