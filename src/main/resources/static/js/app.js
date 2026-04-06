(function () {
    var btn = document.getElementById('navToggle');
    var nav = document.getElementById('headerActions');
    if (!btn || !nav) return;

    btn.addEventListener('click', function () {
        var open = nav.classList.toggle('open');
        btn.setAttribute('aria-expanded', String(open));
        btn.classList.toggle('open', open);
    });

    document.addEventListener('click', function (e) {
        if (nav.classList.contains('open') && !btn.contains(e.target) && !nav.contains(e.target)) {
            nav.classList.remove('open');
            btn.classList.remove('open');
            btn.setAttribute('aria-expanded', 'false');
        }
    });
})();
