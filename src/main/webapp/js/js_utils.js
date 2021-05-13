




function ajaxLoading() {
    var id = "#textboxDiv";
    var left = ($(window).outerWidth(true) - 190) / 2;
    var top = ($(window).height() - 45) / 2;
    var height = $(window).height() * 2;
    $("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: "100%", height: height }).appendTo(id);
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在加载,请稍候...").appendTo(id).css({ display: "block",left: left, top: top });
}

function ajaxLoadEnd(){
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();
}

/* 使用方法:
* 开启:MaskUtil.mask();
* 关闭:MaskUtil.unmask();
*
* MaskUtil.mask('其它提示文字...');
*/
var MaskUtil = (function(){
    var $mask,$maskMsg;
    var defMsg = '正在处理，请稍待。。。';
    function init(){
        if(!$mask){
            $mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo("body");
        }
        if(!$maskMsg){
            $maskMsg = $("<div class=\"datagrid-mask-msg mymask\">"+defMsg+"</div>")
                .appendTo("body").css({'font-size':'12px'});
        }
        $mask.css({width:"100%",height:$(document).height()});
        var scrollTop = $(document.body).scrollTop();
        $maskMsg.css({
            left:( $(document.body).outerWidth(true) - 190 ) / 2
            ,top:( ($(window).height() - 45) / 2 ) + scrollTop
        });
    }
    return {
        mask:function(msg){
            init();
            $mask.show();
            $maskMsg.html(msg||defMsg).show();
        }
        ,unmask:function(){
            $mask.hide();
            $maskMsg.hide();
        }
    }
}());