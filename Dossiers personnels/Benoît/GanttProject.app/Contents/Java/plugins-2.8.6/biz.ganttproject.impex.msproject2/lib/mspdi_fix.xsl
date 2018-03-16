<?xml version="1.0" encoding="iso-8859-2" ?>
<!-- 
This XSLT fixes projects created by Planta model.Project, where <model.Project> element has no associated namespace.
See http://code.google.com/p/ganttproject/issues/detail?id=438
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/model.Project">
  <xsl:element name="model.Project" xmlns="http://schemas.microsoft.com/project">
  <xsl:apply-templates/>
  </xsl:element>
</xsl:template>

<xsl:template match="@*|node()">
  <xsl:copy>
    <xsl:apply-templates/>
  </xsl:copy>
</xsl:template>
</xsl:stylesheet>