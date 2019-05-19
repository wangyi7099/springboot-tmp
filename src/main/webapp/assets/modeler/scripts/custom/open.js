var open = {}
open.query = function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}
open.init = function () {
    var qy = open.query("trigger");
    if (qy != null && qy == 'createProcess') {
        try {
            setTimeout(function () {
                jQuery("#createProcess").click();
            }, 1000);

        } catch (e) {
            console.log(e);
        }
    }
}

jQuery(function () {
    open.init();
})

