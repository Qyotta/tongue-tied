<?xml version="1.0"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:o="urn:schemas-microsoft-com:office:office"
          xmlns:x="urn:schemas-microsoft-com:office:excel"
          xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:html="http://www.w3.org/TR/REC-html40">
    <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
        <Author>TongueTied</Author>
        <LastAuthor>TongueTied</LastAuthor>
        <Created>${currentDate?string("yyyy-MM-dd'T'HH:mm:ss'Z'")}</Created>
        <LastSaved>${currentDate?string("yyyy-MM-dd'T'HH:mm:ss'Z'")}</LastSaved>
        <Company>TongueTied</Company>
        <Version>10.3501</Version>
    </DocumentProperties>
    <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
        <DownloadComponents/>
        <LocationOfComponents HRef="file:///D:\"/>
    </OfficeDocumentSettings>
    <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
        <WindowHeight>12270</WindowHeight>
        <WindowWidth>15240</WindowWidth>
        <WindowTopX>360</WindowTopX>
        <WindowTopY>60</WindowTopY>
        <ProtectStructure>False</ProtectStructure>
        <ProtectWindows>False</ProtectWindows>
    </ExcelWorkbook>
    <Styles>
        <Style ss:ID="Default" ss:Name="Normal">
            <Alignment ss:Vertical="Bottom"/>
            <Borders/>
            <Font ss:FontName="Futura Lt BT"/>
            <Interior/>
            <NumberFormat/>
            <Protection/>
        </Style>
        <Style ss:ID="s21">
            <Alignment ss:Vertical="Bottom" ss:WrapText="1"/>
        </Style>
        <Style ss:ID="s24">
            <Borders>
                <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
                <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
                <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
                <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
            </Borders>
            <Font ss:FontName="Futura Lt BT" x:Family="Swiss" ss:Bold="1"/>
            <Interior ss:Color="#C0C0C0" ss:Pattern="Solid"/>
        </Style>
    </Styles>
    <Worksheet ss:Name="Sheet1">
        <Table ss:ExpandedColumnCount="${languages?size + 4}" ss:ExpandedRowCount="${translations?size + 1}" x:FullColumns="1" x:FullRows="1">
            <Column ss:Width="226.5"/>
            <Column ss:StyleID="s21" ss:AutoFitWidth="0" ss:Width="149.25"/>
            <Column ss:Width="100"/>
            <Column ss:Width="100"/>
            <#list languages as language>
            <Column ss:StyleID="s21" ss:AutoFitWidth="0" ss:Width="250.75"/>
            </#list>
            <Row>
                <Cell ss:StyleID="s24"><Data ss:Type="String">Keyword</Data></Cell>
                <Cell ss:StyleID="s24"><Data ss:Type="String">Context</Data></Cell>
                <Cell ss:StyleID="s24"><Data ss:Type="String">Bundle</Data></Cell>
                <Cell ss:StyleID="s24"><Data ss:Type="String">Country</Data></Cell>
                <#list languages as language>
                <Cell ss:StyleID="s24"><Data ss:Type="String">${language.code}:${language.name}</Data></Cell>
                </#list>
            </Row>
            <#list translations as item>
            <Row>
                <Cell><Data ss:Type="String">${item.keyword}</Data></Cell>
                <Cell><Data ss:Type="String">${item.context!?xml}</Data></Cell>
                <Cell><Data ss:Type="String">${item.bundle.name}</Data></Cell>
                <Cell><Data ss:Type="String">${item.country.code}:${item.country.name}</Data></Cell>
                <#list languages as language>
                    <#assign hasMatch = false>
                    <#list item.translationEntries as entry>
                        <#if language.code == entry.key>
                <Cell><Data ss:Type="String">${entry.value!?xml}</Data></Cell>
                            <#assign hasMatch = true>
                            <#break>
                        </#if>
                    </#list>
                    <#if !hasMatch>
                <Cell><Data ss:Type="String"></Data></Cell>
                    </#if>
                </#list>
            </Row>
            </#list>
        </Table>
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <Selected/>
            <FreezePanes/>
            <FrozenNoSplit/>
            <SplitHorizontal>1</SplitHorizontal>
            <TopRowBottomPane>1</TopRowBottomPane>
            <ActivePane>2</ActivePane>
            <Panes>
                <Pane>
                    <Number>3</Number>
                    <ActiveRow>1</ActiveRow>
                    <ActiveCol>1</ActiveCol>
                </Pane>
            </Panes>
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>
    <Worksheet ss:Name="Sheet2">
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>
    <Worksheet ss:Name="Sheet3">
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>
</Workbook>
