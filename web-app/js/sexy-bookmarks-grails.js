/*
	click handler for SexyBookmarks
	Credit: Phong Thai Cao - http://www.JavaScriptBank.com
	Please keep this credit when you use this code
*/

jQuery('.sexy-bookmarks a.external').click(function() {
	// get the current URL & encode it into the standard URI
	var url = encodeURIComponent(window.location.href), desc = '';

	// parse the description for the above URL by the text() method of jQuery
	// the text must be placed in the P tag with ID="sexy-bookmarks-content"
	// so you can change the container of description with another tag and/or another ID
	if( jQuery('p.sexy-bookmarks-content').length ) {
		desc = encodeURIComponent(jQuery('p.sexy-bookmarks-content').text());
	}

	// detect the social bookmark site user want to share your URL
	// by checking the className of site that we'll declare in the HTML code
	// and assign the URL & description we got into the current anchor
	// then redirect to the clicked bookmark site, you can use window.open() method for opening the new window
	switch(this.parentNode.className) {
		case 'sexy-twittley':
			this.href += '?title=' + document.title + '&url=' + url + '&desc=' + desc + '&pcat=Internet&tags=';
			break;
		case 'sexy-digg':
			this.href += '?phase=2&title=' + document.title + '&url=' + url + '&desc=' + desc;
			break;
		case 'sexy-twitter':
			this.href += '?status=RT+@your_twitter_id:+' + document.title + '+-+' + url;
			break;
		case 'sexy-scriptstyle':
			this.href += '?title=' + document.title + '&url=' + url;
			break;
		case 'sexy-reddit':
			this.href += '?title=' + document.title + '&url=' + url;
			break;
		case 'sexy-delicious':
			this.href += '?title=' + document.title + '&url=' + url;
			break;
		case 'sexy-stumbleupon':
			this.href += '?title=' + document.title + '&url=' + url;
			break;
		case 'sexy-mixx':
			this.href += '?title=' + document.title + '&page_url=' + url + '&desc=' + desc;
			break;
		case 'sexy-technorati':
			this.href += '?add=' + url;
			break;
		case 'sexy-blinklist':
			this.href += '?Action=Blink/addblink.php&Title=' + document.title + '&Url=' + url;
			break;
		case 'sexy-diigo':
			this.href += '?title=' + document.title + '&url=' + url + '&desc=' + desc;
			break;
		case 'sexy-yahoobuzz':
			this.href += '?submitHeadline=' + document.title + '&submitUrl=' + url + '&submitSummary=' + desc + '&submitCategory=science&submitAssetType=text';
			break;
		case 'sexy-myspace':
			this.href += '?t=' + document.title + '&u=' + url;
			break;
		case 'sexy-facebook':
			this.href += '?t=' + document.title + '&u=' + url;
			break;
		case 'sexy-designfloat':
			this.href += '?title=' + document.title + '&url=' + url;
			break;
		case 'sexy-devmarks':
			this.href += '?posttitle=' + document.title + '&posturl=' + url + '&posttext=' + desc;
			break;
		case 'sexy-newsvine':
			this.href += '?h=' + document.title + '&u=' + url;
			break;
		case 'sexy-google':
			this.href += '?op=add&title=' + document.title + '&bkmk=' + url;
			break;
	}
})
