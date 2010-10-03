package sexy.bookmarks

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class SexyBookmarksTagLib {

  static def namespace = 'sexy'

  def initialise = { attrs, body ->
    out << g.javascript([library:'jquery', plugin:'jquery'], body())
    out << g.javascript([library:'sexy-bookmarks-public'], body())
    out << g.javascript([library:'sexy-bookmarks-grails'], body())
    out << "<link rel='stylesheet' type='text/css' href='${resource(dir:'css', file:'style.css')}' media='screen, projector' />"
  }

  def bookmarks = { attrs, body ->

    List<String> include = listIncludedServices(attrs)
    Map<String, String> replacements = buildReplacementsList(attrs)

    out << '''
      <div class="shr-bookmarks shr-bookmarks-expand shr-bookmarks-center shr-bookmarks-bg-sexy">
      <ul class="socials">
       '''
        writeHtmlList(include, replacements)
        out << '''
      </ul>
      </div>
    '''
  }

  private List writeHtmlList(List<String> include, Map<String, String> replacements) {
    return include.each { service ->

      def details = CH.config.sexy.bookmarks[service]
      String parsedUrl = buildUrl(details.url, replacements)

      out << "<li class='shr-${service}'><a href='${parsedUrl}' rel='nofollow' class='external' title='${details.share}'>${details.share}</a></li>"

    }
  }

  private List<String> listIncludedServices(attrs) {
    List<String> include = attrs.include?.split('\\,') ?: []

    if (include.isEmpty()) {
      include = CH.config.sexy.bookmarks.enabled.collect { String service, String enabled ->
        if (enabled == 'true') { return service }
      }
    }

    if (attrs.exclude) { include.removeAll(attrs.exclude?.split('\\,')) }
    return include
  }

  private Map<String, String> buildReplacementsList(attrs) {
    Map<String, String> replacements = [
            'PERMALINK': attrs.permaLink.encodeAsURL(),
            'TITLE': attrs.title.encodeAsURL(),
            'SHORT_TITLE': attrs.title.encodeAsURL(),
            'TEASER': attrs.teaser.encodeAsURL(),
            'YAHOOTEASER': attrs.teaser.encodeAsURL(),
            'YAHOOCATEGORY': attrs.yahooCategory.encodeAsURL(),
            'YAHOOMEDIATYPE': attrs.yahooMediaType.encodeAsURL(),
            'POST_SUMMARY': attrs.summary.encodeAsURL(),
            'SITE_NAME': attrs.siteName.encodeAsURL(),
            'TWITT_CAT': attrs.twitterCategory.encodeAsURL(),
            'DEFAULT_TAGS': attrs.tags.encodeAsURL(),
            'FETCH_URL': attrs.from.encodeAsURL()
    ]
    return replacements
  }

  private String buildUrl(String url, Map<String, String> replacements) {

      String parsedUrl = url
      replacements.each { String token, String value ->
        parsedUrl = parsedUrl.replaceAll(token, value)
      }
      return parsedUrl
  }

}
