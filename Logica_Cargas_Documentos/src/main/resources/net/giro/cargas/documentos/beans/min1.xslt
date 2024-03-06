<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="UTF-8" indent="yes" method="xml" version="1.0"/>
	<xsl:strip-space elements="*"/>
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞŸŽŠŒ'"/>
	<xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyzàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿžšœ'"/>
	<xsl:template match="*">
		<xsl:element name="{translate(local-name(), $uppercase, $lowercase)}" namespace="{namespace-uri()}">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="@*">
		<xsl:attribute name="{translate(local-name(), $uppercase, $lowercase)}" namespace="{namespace-uri()}">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="comment() | text() | processing-instruction()">
		<xsl:copy/>
	</xsl:template>
</xsl:stylesheet>