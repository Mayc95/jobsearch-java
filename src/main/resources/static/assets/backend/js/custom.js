/**
 *
 * You can write your JS code here, DO NOT touch the default style file
 * because it will make it harder for you to update.
 *
 */

"use strict";

$("#statsSlider1").owlCarousel({
    items: 4,
    margin: 10,
    autoplay: true,
    nav: true,
    dots: false,
    autoplayTimeout:5000,
    autoplayHoverPause:true,
    navText: [
        '<i class="fas fa-chevron-left"></i>',
        '<i class="fas fa-chevron-right"></i>',
    ],
});
