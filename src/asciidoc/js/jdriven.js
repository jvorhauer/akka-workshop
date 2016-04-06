//Executes your code when the DOM is ready.  Acts the same as $(document).ready().

$(function() {
	//First add the JDriven progress bar and TOC
	$('#header').after('<div class="progress" style="display: block;"><span style="width: 0px;"></span></div>');
	$('#content').before('<div id="toc" class="toc2"></div>');
	//Then call the tocify method on your HTML div to automatically generate a TOC in the TOC div
	$('#toc').tocify({ smoothScroll: true, selectors: "h2,h3", scrollTo: 92 });
	$('#toc li').hover(
	  function() {
		$(this).animate({
          backgroundColor: "#DFEFC3",
        }, 130 );
	  },
	  function() {
		var backgroundClr = "#FFFFFF";
		if($(this).hasClass('active')) {
			backgroundClr = "#DFEFC3";
		} 
		$(this).animate({
          backgroundColor: backgroundClr,
        }, 130, function(){
			$(this).css("background-color", "");
		});
	  }
	);
	
	function updateProgress() {
		var maxScrollHeight = $(document).height() - $(window).height();
		var percent =  ($(window).scrollTop() * 100) / maxScrollHeight;
		var progressWidth = ($(window).width() * percent) / 100;
		$(".progress span").css( "width", progressWidth);
		if($(window).scrollTop() + $(window).height() == $(document).height()) {
			$(".progress span").css( "width", $(window).width());
		}		
	}
	$(window).resize(function(){
		updateProgress();
	});
	$(window).scroll(function(){
		updateProgress();
	});	
});