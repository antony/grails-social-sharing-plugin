package uk.co.desirableobjects.grails.sexy.bookmarks

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.apache.jasper.compiler.ELParser.Char

class SexyBookmarksTagLib {

  static def namespace = 'sexy'

  def initialise = { attrs, body ->
    out << g.javascript([library:'jquery', plugin:'jquery'], body())
    out << g.javascript([library:'sexy-bookmarks-public'], body())
    out << g.javascript([library:'sexy-bookmarks-grails'], body())
    out << "<link rel='stylesheet' type='text/css' href='${resource(dir:'css', file:'style.css')}' media='screen, projector' />"
  }

  def bookmarks = { attrs, body ->

    Collection<String> include = listIncludedServices(attrs)
    Map<String, String> replacements = buildReplacementsList(attrs)
    String sloganCss = fetchSloganClass(attrs.slogan)

    out << writeHtml(sloganCss, writeHtmlList(include, replacements))
  }

  private String writeHtmlList(Collection<String> include, Map<String, String> replacements) {
    StringBuffer buffer = new StringBuffer()
    include.each { service ->

      def details = CH.config.sexy.bookmarks[service]
      println details
      String parsedUrl = buildUrl(details.url, replacements)

      buffer.append("<li class='shr-${service}'><a href='${parsedUrl}' rel='nofollow' class='external' title='${details.share}'>${details.share}</a></li>")
      includeJs(buffer, service, details)
    }
    return buffer.toString()
  }

  private Collection<String> listIncludedServices(attrs) {
    Collection<String> include = attrs.include?.split('\\,') ?: []

    if (include.isEmpty()) {
      include = CH.config.sexy.bookmarks.enabled.collect { String service, String enabled ->
        if (enabled == 'true') { return service }
      }
    }

    if (attrs.exclude) { include.removeAll(attrs.exclude?.split('\\,')) }
    return include
  }

  private LinkedHashMap<String, String> buildReplacementsList(attrs) {
    Map<String, String> replacements = [
            'PERMALINK': attrs.permaLink.encodeAsURL(),
            'SHORT_TITLE': attrs.title.encodeAsURL(),
            'TITLE': attrs.title.encodeAsURL(),
            'YAHOOTEASER': attrs.teaser.encodeAsURL(),
            'TEASER': attrs.teaser.encodeAsURL(),
            'YAHOOCATEGORY': attrs.yahooCategory.encodeAsURL(),
            'YAHOOMEDIATYPE': attrs.yahooMediaType.encodeAsURL(),
            'POST_SUMMARY': attrs.summary.encodeAsURL(),
            'SITE_NAME': attrs.siteName.encodeAsURL(),
            'TWITT_CAT': attrs.twitterCategory.encodeAsURL(),
            'DEFAULT_TAGS': attrs.tags.encodeAsURL(),
            'FETCH_URL': attrs.from.encodeAsURL()
    ] as LinkedHashMap<String, String>
    return replacements
  }

  private String buildUrl(String url, Map<String, String> replacements) {

      String parsedUrl = url
      replacements.each { String token, String value ->
        parsedUrl = parsedUrl.replaceAll(token, value)
      }
      return parsedUrl
  }

  private String fetchSloganClass(String slogan) {
    if (!slogan?.isEmpty()) {
      return slogan == 'sexy' ? 'shr-bookmarks-bg-shr' : "shr-bookmarks-bg-${slogan}"
    }
    return ""
  }

  private String writeHtml(String sloganCss, String serviceList) {
    return """
      <div class='shr-bookmarks shr-bookmarks-expand shr-bookmarks-center ${sloganCss}'>
      <ul class='socials'>
        ${serviceList}
      </ul>
      </div>
    """
  }

  private String includeJs(StringBuffer buffer, String service, ConfigObject details) {
    if (!details.onclick?.isEmpty()) {
      buffer.append(g.javascript([:], { return """
        jQuery('.shr-${service} a.external').click(function() {
          ${details.onclick}
        });
      """ }))
    }
  }

}
