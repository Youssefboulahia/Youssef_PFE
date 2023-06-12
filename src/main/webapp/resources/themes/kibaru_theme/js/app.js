"use strict";

function createDonutCharts() {
  $("<style type='text/css' id='dynamic' />").appendTo("head");
  $("div[chart-type*=donut]").each(function () {
    var d = $(this);
    var id = $(this).attr('id');
    var max = $(this).data('chart-max');
    if ($(this).data('chart-text')) {
      var text = $(this).data('chart-text');
    } else {
      var text = "";
    }
    if ($(this).data('chart-caption')) {
      var caption = $(this).data('chart-caption');
    } else {
      var caption = "";
    }
    if ($(this).data('chart-initial-rotate')) {
      var rotate = $(this).data('chart-initial-rotate');
    } else {
      var rotate = 0;
    }
    var segments = $(this).data('chart-segments');

    for (var i = 0; i < Object.keys(segments).length; i++) {
      var s = segments[i];
      var start = ((s[0] / max) * 360) + rotate;
      var deg = ((s[1] / max) * 360);
      if (s[1] >= (max / 2)) {
        d.append('<div class="large donut-bite" data-segment-index="' + i + '"> ');
      } else {
        d.append('<div class="donut-bite" data-segment-index="' + i + '"> ');
      }
      var style = $("#dynamic").text() + "#" + id + " .donut-bite[data-segment-index=\"" + i + "\"]{-moz-transform:rotate(" + start + "deg);-ms-transform:rotate(" + start + "deg);-webkit-transform:rotate(" + start + "deg);-o-transform:rotate(" + start + "deg);transform:rotate(" + start + "deg);}#" + id + " .donut-bite[data-segment-index=\"" + i + "\"]:BEFORE{-moz-transform:rotate(" + deg + "deg);-ms-transform:rotate(" + deg + "deg);-webkit-transform:rotate(" + deg + "deg);-o-transform:rotate(" + deg + "deg);transform:rotate(" + deg + "deg); background-color: " + s[2] + ";}#" + id + " .donut-bite[data-segment-index=\"" + i + "\"]:BEFORE{ background-color: " + s[2] + ";}#" + id + " .donut-bite[data-segment-index=\"" + i + "\"].large:AFTER{ background-color: " + s[2] + ";}";
      $("#dynamic").text(style);
    }

    d.children().first().before("<div class='donut-hole'><span class='donut-filling'>" + text + "</span></div>");
    d.append("<div class='donut-caption-wrapper'><span class='donut-caption'>" + caption + "</span></div>");
  });
}

$(document).ready(function () {

  // add meta (responsive)
  $("head").append(
    '<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">'
  );
  $("head").append(
    '<meta http-equiv="X-UA-Compatible" content="IE=edge">'
  );
  createDonutCharts();
  //popover init
  $(function () {
    try {
      $('[data-toggle="popover"]').popover();
    } catch (e) {}
  });

  $("#FilterSideT").on("click", function () {
    if ($('.filterSide').is('.on')) {
      $('.filterSide').removeClass("on");
      $('.filterSide').slideUp(200);
    } else {
      $('.filterSide').addClass("on");
      $('.filterSide').slideDown(200);
    }
  });
  $(document).on("click", ".timelinerefresh", function (e) {
    e.preventDefault();
    $(".loaderTimeline").css({
      display: 'flex'
    });
    var client = new MashupAjaxClient(this, {
      spinner: false,
      success: function () {
        $(this).removeClass('on');
        $(".loaderTimeline").css({
          display: 'none'
        });

        if ($(".filterSide   .refined").length > 0) {
          $(".titleTimeline   #FilterSideT .fas").addClass("icon_refined");
          $(".filterSide .raz").show();
        } else {
          $(".titleTimeline   #FilterSideT .fas").removeClass("icon_refined");
        }
      }
    });
   
    client.addWidget("filterSide");
    client.addWidget("ajaxTimeLine");
    client.update();
  });

  $(document).on("click", "#parcs", function () {
    if (!$(this).is(".on")) {
      $(this).addClass('on');

      $(".parcListIframe").slideUp(200);
    } else {
      $(this).removeClass('on');
      var iframe = $('.InfoHeaderIframe iframe').contents();
      iframe.find("#CloseCustomerInfo").click();
      $(".customDiv.fullwidth .spanfullwidth.on").click();
      $(".parcListIframe").slideDown(200);
      $(".scrollDiv").animate({
        scrollTop: 0
      }, "slow");
    }
  });

  $(document).on("click", "#Closeparcs", function () {
    $("#parcs").click();
  });

  // clientInfos Show Hide
  $(".clientInfos ").parent().addClass("position-relative");
  $(".clientInfos ").parent().prepend('<i class="fas fa-chevron-up clientInfosShowHide"></i>');
  $(document).on("click", ".clientInfosShowHide", function () {
    if (!$(this).is(".on")) {
      $(this).addClass('on');
      $(".clientInfos ").slideUp(200);
      setTimeout(function () {
        $('.scrollDiv').css('max-height', $(window).height() - $('.header ').height() - $('.fixedDiv').height() - 10);
        $('.socialTimeline').css('max-height', $(window).height() - $('.header ').height() - $('.info ').height() - 37);

      }, 400); } else {
      $(this).removeClass('on');
      $(".clientInfos ").slideDown(200);
      setTimeout(function () {
        $('.scrollDiv').css('max-height', $(window).height() - $('.header ').height() - $('.fixedDiv').height() - 10);
        $('.socialTimeline').css('max-height', $(window).height() - $('.header ').height() - $('.info ').height() - 37);

      }, 400);}
  });
  $(document).on("click", "#InfoHeaderIframe", function () {
    if (!$(this).is(".on")) {
      $(this).addClass('on');

      $(".InfoHeaderIframe").slideUp(200);
    } else {
      $(this).removeClass('on');
      $("#Closeparcs:visible").click();
      $(".customDiv.fullwidth .spanfullwidth.on").click();
      $(".InfoHeaderIframe").slideDown(200);
      $(".scrollDiv").animate({
        scrollTop: 0
      }, "slow");
    }
  });
  $(document).on("click", ".ellipsisText", function (e) {
    if (!$(this).is(".ShowAllText")) {
      $(this).addClass("ShowAllText");
    } else {
      $(this).removeClass("ShowAllText");
    }
  });
  $("#search_MSISDN").parents("form").on("submit", function(){
		$(".loaderPark").css({
      display: 'flex'
    });
  });


  $(document).on("click", ".contractPerCustomer table a", function (e) {
    e.preventDefault();
    $(".loaderPark").css({
      display: 'flex'
    });
    window.history.pushState(null, null, $(this).attr("href"));
    //$(".loader").show();
    var client = new MashupAjaxClient(this, {
      spinner: false,
      success: function () {
        $(this).addClass('on');
        $(".installedPac  #parcs").click();
        $(".b-contratDivInternet, .b-consoDivInternet").css('height', $('.b-factuDiv').height() - $('.b-userDiv').height() - 9);
        $(".loaderPark").hide();
      }
    });


    client.addWidget("reload");
    client.update();
  });


  $(document).on("click", ".filterSide  a", function (e) {
    e.preventDefault();
    $(".loaderTimeline").css({
      display: 'flex'
    });
    var Url = new BuildUrl($(this).attr("href"));
    Url.removeParameter("perPage");
    window.history.pushState(null, null, Url.toString());
    var client = new MashupAjaxClient(this, {
      spinner: false,
      success: function () {
        $(this).removeClass('on');
        $(".filterSide").slideUp(200);
        $(".loaderTimeline").css({
          display: 'none'
        });
       
        if ($(".filterSide   .refined").length > 0) {
          $(".titleTimeline   #FilterSideT .fas").addClass("icon_refined");
          $(".filterSide .raz").show();
        } else {
          $(".titleTimeline   #FilterSideT .fas").removeClass("icon_refined");
        }
      }
    });
    client.url.removeParameter("perPage");
    client.addWidget("filterSide");
    client.addWidget("ajaxTimeLine");
    client.update();
  });

  $(document).on("click", ".parcListIframe .facetingList  a", function (e) {
    e.preventDefault();
    var link = $(this).attr("href");
    
    $(".loaderPark").css({
      display: 'flex'
    });
    window.history.pushState(null, null, link);
    var client = new MashupAjaxClient(this, {
      spinner: false,
      success: function () {
        $(".parcListIframe .filterIcon").click();
        $('.contractPerCustomer table tr ').each(function () {
          $('.contractPerCustomer table tbody tr td:contains("ACTIF") ').each(function () {
            $(this).parents("tr").addClass("actif");
          });
          $('.contractPerCustomer table tbody tr td:contains("SUSPENDU") ').each(function () {
            $(this).parents("tr").addClass("suspendu");
          });
          $('.contractPerCustomer table tbody tr td:contains("RESILIE") ').each(function () {
            $(this).parents("tr").addClass("inactive");
          });
        });

        if ($(".parcListIframe .exa-faceting-list-li-refined").length > 0) {
          $(".parcListIframe  .filterIcon .fas").addClass("icon_refined");
          $(".facets  .raz").show();
        } else {
          $(".parcListIframe  .filterIcon .fas").removeClass("icon_refined");
        }
        $(".loaderPark").hide();

      },
    });
    client.url.removeParameter("_")
    client.addWidget("contractPerCustomerReload");
    client.update();

  });

  $(document).on("click", ".parcListIframe .filterIcon", function (e) {
    $(".facets").toggleClass("on");
  });


});
//close popOver
$('body').on('click', function (e) {
  $('.openPopover').each(function () {
    if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
      $(this).popover('hide');
    }
  });
});
//close notification contrat
$(document).click(function (e) {
  if ($(e.target).attr("id") != "ContratNotification" &&
    $(e.target).parents(".ContratNotification").length === 0) {
    $("#ContratNotification").removeClass("on");
    $(".ContratNotification").slideUp();
  }
});
//close notification 
$(document).click(function (e) {
  if (!$(e.target).hasClass("alLnotifications") &&
    $(e.target).parents(".notifDisplay").length === 0) {
    $(".notifDisplay ").slideUp();
    $(".alLnotifications").removeClass("on");
  }
});
$(document).on("click", ".customerInfoHeader .openPopover", function () {
  $('.customerInfoHeader .popover-body .row [class*="col"]  ').each(function () {
    if ($(this).html().length > 20) {
      $(this).addClass("ellipsisText");
    }
  });
});
$("#btnCrm").on("click", function () {
  if (!$("#DivCrmApp").is(".on")) {
    $("#DivCrmApp").addClass("on");
    $("#DivCrmApp").slideDown();
  } else {
    $("#DivCrmApp").removeClass("on");
    $("#DivCrmApp").slideUp();
  }
});


$(document).click(function (e) {
  if (!$(e.target).hasClass("btn") &&
    $(e.target).parents("#DivCrmApp").length === 0) {
    $("#DivCrmApp").removeClass("on");
    $("#DivCrmApp").slideUp();
  }
});


var loaded = false;
$(window).load(function () {
  loaded = true;
  if (window.location.search.indexOf("msisdn=") <= -1) {
    $("#parcs").click();
  }
  $(".withHudIcon > .information > label").append('<div class="divHud"> <a href="#" class="bntHud"><img  src="../resources/widgets/kibaru_commonRessources/boosted-orange/img/network.png" /></a></div>');
  $('.scrollDiv').css('max-height', $(window).height() - $('.header ').height() - $('.fixedDiv').height() - 10);
  $('.socialTimeline').css('max-height', $(window).height() - $('.header ').height() - $('.info ').height() - 37);

  $(".b-moneyDivPP , .b-seddoDivPP").css('height', (($('.b-consoDivPP').parent(".row").height() - $(".b-contratDivPP").height()) / 2));
  $(".b-contratDivInternet, .b-consoDivInternet").css('height', $('.b-factuDiv').height() - $('.b-userDiv').height() - 9);
  if ($('.b-consoDivPP').length > 0) {
    $(".b-moneyDiv , .b-seddoDiv").css('height', (($('.b-consoDivPP').parent(".row").height() - $(".b-contratDiv").height()) / 2));
  }
  if ($('.b-factuDivInternet').length > 0) {
    $(document).on("click", ".spanfullwidth", function (e) {
      $("[class*=toHideLoader_]").attr("style", "");
    });
  }
  
  if (window.location.href.indexOf("customersearch") > -1) {
    $('.contentWrapper .meta:contains("Inactif")').parents(".contentWrapper").addClass("inactiveCustomer");
  }
  if (window.location.href.indexOf("client_code") > -1) {
     $('.contractPerCustomer table tbody tr td:contains("ACTIF") ').each(function () {
            $(this).parents("tr").addClass("actif");
          });
          $('.contractPerCustomer table tbody tr td:contains("SUSPENDU") ').each(function () {
            $(this).parents("tr").addClass("suspendu");
          });
          $('.contractPerCustomer table tbody tr td:contains("RESILIE") ').each(function () {
            $(this).parents("tr").addClass("inactive");
          });

  }
  $(".nbrToformat").each(function (index) {
    $(this).html(numberWithSpaces($(this).html()));
  });
  $(".loader").hide();

   
});




function getAllUrlParams(url) {
  var queryString = url ? url.split('?')[1] : window.location.search.slice(1);

  var obj = {};

  if (queryString) {

    queryString = queryString.split('#')[0];

    var arr = queryString.split('&');

    for (var i = 0; i < arr.length; i++) {
      var a = arr[i].split('=');

      var paramNum = undefined;
      var paramName = a[0].replace(/\[\d*\]/, function (v) {
        paramNum = v.slice(1, -1);
        return '';
      });

      var paramValue = typeof (a[1]) === 'undefined' ? true : a[1];

      paramName = paramName.toLowerCase();
      paramValue = paramValue.toLowerCase();

      if (obj[paramName]) {
        if (typeof obj[paramName] === 'string') {
          obj[paramName] = [obj[paramName]];
        }
        if (typeof paramNum === 'undefined') {
          obj[paramName].push(paramValue);
        } else {
          obj[paramName][paramNum] = paramValue;
        }
      } else {
        obj[paramName] = paramValue;
      }
    }
  }

  return obj;
}

var getParams = function (url) {
  var params = {};
  var parser = document.createElement('a');
  parser.href = url;
  var query = parser.search.substring(1);
  var vars = query.split('&');
  for (var i = 0; i < vars.length; i++) {
    var pair = vars[i].split('=');
    params[pair[0]] = decodeURIComponent(pair[1]);
  }
  return params;
};

function numberWithSpaces(x) {
  var parts = x.toString().split(".");
  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, " ");
  return parts.join(".");
}

// datatable

$(document).ready(function () {
  $('.datatable').DataTable({
    "bPaginate": true,
    "bLengthChange": false,
    "bFilter": true,
    "bInfo": false,
    "bAutoWidth": false,
    searching: false,
    "aaSorting": [],
    "order": [],
    "language": {
      "sProcessing": "Traitement en cours...",
      "sSearch": "Rechercher&nbsp;:",
      "sLengthMenu": "Afficher _MENU_ &eacute;l&eacute;ments",
      "sInfo": "Affichage de l'&eacute;l&eacute;ment _START_ &agrave; _END_ sur _TOTAL_ &eacute;l&eacute;ments",
      "sInfoEmpty": "Affichage de l'&eacute;l&eacute;ment 0 &agrave; 0 sur 0 &eacute;l&eacute;ment",
      "sInfoFiltered": "(filtr&eacute; de _MAX_ &eacute;l&eacute;ments au total)",
      "sInfoPostFix": "",
      "sLoadingRecords": "Chargement en cours...",
      "sZeroRecords": "Aucun &eacute;l&eacute;ment &agrave; afficher",
      "sEmptyTable": "Aucune donn&eacute;e disponible dans le tableau",
      "oPaginate": {
        "sFirst": '<i class="fas fa-angle-double-left"></i>',
        "sPrevious": '<i class="fas fa-chevron-left"></i>',
        "sNext": '<i class="fas fa-chevron-right"></i>',
        "sLast": '<i class="fas fa-angle-double-right"></i>'
      },
      "oAria": {
        "sSortAscending": ": activer pour trier la colonne par ordre croissant",
        "sSortDescending": ": activer pour trier la colonne par ordre d&eacute;croissant"
      },
      "select": {
        "rows": {
          _: "%d lignes séléctionnées",
          0: "Aucune ligne séléctionnée",
          1: "1 ligne séléctionnée"
        }
      }
    }
  });


});




function TableDeco() {
  if ($(".contractPerCustomer table tbody tr").length > 0 && (!$(".contractPerCustomer table tbody tr").hasClass("actif") || !$(".contractPerCustomer table tbody tr").hasClass("suspendu") > 0 || !$(".contractPerCustomer table tbody tr").hasClass("inactive"))) {
     $('.contractPerCustomer table tbody tr td:contains("ACTIF") ').each(function () {
            $(this).parents("tr").addClass("actif");
          });
          $('.contractPerCustomer table tbody tr td:contains("SUSPENDU") ').each(function () {
            $(this).parents("tr").addClass("suspendu");
          });
          $('.contractPerCustomer table tbody tr td:contains("RESILIE") ').each(function () {
            $(this).parents("tr").addClass("inactive");
          });
  } else {
    setTimeout(function () {
      TableDeco();
    }, 500);
  }
}

$('body').on('DOMSubtreeModified', '.contractPerCustomer  table', function (e) {

  TableDeco();

});