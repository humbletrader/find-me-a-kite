
var chooseItemText = "Choose..."
var criteria = ["brand", "product_name", "subprod_name", "version", "year", "size", "condition", "price"];
var criteriaCount = 0;
var criteriaDivIdPrefix = "criteriaRow";
var criteriaValueIdPrefix = "criteria";
var criteriaOperatorIdPrefix = "criteriaOperator";
var criteriaNameIdPrefix = "criteriaValue";
var NO_SUPPORTER = "none";

function disableFilterInputsForLevel(level){
    console.log("disabling input #"+criteriaNameIdPrefix+level)
    $("#"+criteriaNameIdPrefix+level).attr("disabled", "disabled");
    $("#"+criteriaValueIdPrefix+level).attr("disabled", "disabled");
}

function previousFormsHaveCorrectStatus(){
    if(criteriaCount > 0){
        var selectedCriteria = $("#"+criteriaNameIdPrefix + (criteriaCount-1)).val();
        var selectedCriteriaValue = $("#"+criteriaValueIdPrefix + (criteriaCount-1)).val();
        console.log("selected criteria: "+selectedCriteria +" and value: "+selectedCriteriaValue);
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

    //select with criteria ( brand, product, etc)
    var selectHtmlForCriteria = $("<select onchange='populateDistinctValues(\""+criteriaCount+"\")'>")
        .attr("id",  criteriaNameIdPrefix+criteriaCount)
        .attr("class", "form-control")
        .appendTo(firstColumn);
    $("<option>").val("none").text(chooseItemText).appendTo(selectHtmlForCriteria);

    criteria.forEach(item => {
            $("<option>").val(item).text(item).appendTo(selectHtmlForCriteria);
    });

    var inputGroupAppendInFirstCol = $("<div class='input-group-append'>").appendTo(firstColumn)
    //select with operators

    var selectHtmlForOperators = $("<select id=\""+criteriaOperatorIdPrefix+criteriaCount+"\">")
                .attr({
                    style: "background: none !important;",
                    //class: "form-control"
                })
                .appendTo(inputGroupAppendInFirstCol);
    $("<option>").val("eq").text("=").attr("selected", "selected").appendTo(selectHtmlForOperators);
    $("<option>").val("lte").text("<").appendTo(selectHtmlForOperators);
    $("<option>").val("gte").text(">").appendTo(selectHtmlForOperators);

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
        //console.log("removing row "+i+" from criteria")
        $("#"+criteriaDivIdPrefix+i).remove();
    }
    //console.log("removing "+(criteriaCount - divCount)+" from criteria count")
    criteriaCount = criteriaCount - (criteriaCount - divCount);
    //console.log("criteriaCount "+criteriaCount)
}

function collectCriteriaValues(divCount){
    var criteria = {
        "category" : [{"value" : $("#category").val(), "op": "eq"}],
        "country" : [{"value": $("#country").val(), "op": "eq"}]
    }

    for(i=0; i < divCount; i++){
        var selectedCriteria = $("#"+criteriaNameIdPrefix + i).val();
        var selectedOperator = $("#"+criteriaOperatorIdPrefix + i).val();
        var selectedCriteriaValue = $("#"+criteriaValueIdPrefix + i).val();
        var newValueAndOperator = {"value": selectedCriteriaValue, "op": selectedOperator};

        if(criteria[selectedCriteria]) {
            criteria[selectedCriteria].push(newValueAndOperator);
        }else{
            criteria[selectedCriteria] = [newValueAndOperator];
        }
    }

    return criteria;
}

function collectSupporterToken(){
    const urlParams = new URLSearchParams(window.location.search);
    if(urlParams.has("supporter")){
        return urlParams.get("supporter");
    } else {
        return NO_SUPPORTER;
    }
}

// 1. get the current search criteria
// 2. call server for distinct values for selected colum
// 3. display distinct values
function populateDistinctValues(divCount){
    //delete previous options in select
    $("#"+criteriaValueIdPrefix+divCount).empty();
    $("<option>").val("none").text(chooseItemText).appendTo("#"+criteriaValueIdPrefix+divCount);

    var currentCriteria = $("#"+criteriaNameIdPrefix + divCount).val();

    //if there's no criteria selected we skip the network call
    if(currentCriteria === "none") return;
    else if(currentCriteria === "price"){
        //for price we don't call the distinct values endpoint
        var criteriaValueHtmlSelect = $("#"+criteriaValueIdPrefix+divCount);
        criteriaValueHtmlSelect.removeAttr("disabled");
        for(var price=0; price<4000; price+=200){
            var priceOption = $("<option>").text(price)
            if(price === 1000){
                priceOption.attr("selected", "selected")
            }
            priceOption.appendTo(criteriaValueHtmlSelect);
        }
        return;
    }

    //so it's not price -> select input ( call to server for distinct values)
    var postData = {
        "target": currentCriteria,
        "criteria" : collectCriteriaValues(divCount)
    }

    console.log("populate distinct values for " + JSON.stringify(postData));

     $.ajax({
            url: "/searchDistinctValues",
            contentType : "application/json",
            data: JSON.stringify(postData),
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
            console.log( xhr );
         })
         .always(function( xhr, status ) {
            console.log( "The request is complete!" );
         });
}

//find the next page of data
//1. collect search criteria
//2. call to server for the given criteria
//3. display items in table
function find(pageToFind){

    if(!previousFormsHaveCorrectStatus()){
        alert("please fill all forms before search !");
        return;
    }

    $("#searchResults").empty();

   var postData = {
    "page" : pageToFind,
    "supporterToken" : collectSupporterToken(),
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
            linkTd.append($("<a>").text(item.brandNameVersion).attr("href", item.link).attr("target", "_blank").attr("rel", "noreferrer"));
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
        console.log( xhr );
   })
   .always(function( xhr, status ) {
       console.log( "The request is complete!" );
   });
}

function saveNotification(){
    var supporterToken = collectSupporterToken();

    if(supporterToken === NO_SUPPORTER){
        alert("Notifications are only for supporters !");
        return;
    }

    var email = prompt("Email:")
    var postData = {
        "supporterToken" : collectSupporterToken(),
        "criteria" : collectCriteriaValues(criteriaCount),
        "email" : email
       }
       console.log("sending to server..."+JSON.stringify(postData));
       var ajaxCallResult = $.ajax({
            url: "/notification/save",
            contentType : "application/json",
            data: JSON.stringify(postData),
            type: "POST",
            dataType : "json",
       })
       .done(function(saveNotificationResult) {
            console.log(saveNotificationResult);
            alert(saveNotificationResult.status + ": "+ saveNotificationResult.reason);
       })
       .fail(function( xhr, status, errorThrown ) {
            console.log( "Error: " + errorThrown );
            console.log( "Status: " + status );
            console.dir( xhr );
            alert("Error: "+errorThrown);
       });
       return ajaxCallResult;
}

window.onload = function(){
    $("#newsModalDialog").modal()
}

