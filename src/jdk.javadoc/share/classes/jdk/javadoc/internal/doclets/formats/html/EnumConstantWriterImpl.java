/*
 * Copyright (c) 2003, 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jdk.javadoc.internal.doclets.formats.html;


import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import jdk.javadoc.internal.doclets.formats.html.markup.ContentBuilder;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlTree;
import jdk.javadoc.internal.doclets.formats.html.markup.Text;
import jdk.javadoc.internal.doclets.toolkit.Content;
import jdk.javadoc.internal.doclets.toolkit.EnumConstantWriter;
import jdk.javadoc.internal.doclets.toolkit.MemberSummaryWriter;

/**
 * Writes enum constant documentation in HTML format.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class EnumConstantWriterImpl extends AbstractMemberWriter
    implements EnumConstantWriter, MemberSummaryWriter {

    public EnumConstantWriterImpl(SubWriterHolderWriter writer, TypeElement typeElement) {
        super(writer, typeElement);
    }

    public EnumConstantWriterImpl(SubWriterHolderWriter writer) {
        super(writer);
    }

    @Override
    public Content getMemberSummaryHeader(TypeElement typeElement,
            Content content) {
        content.add(MarkerComments.START_OF_ENUM_CONSTANT_SUMMARY);
        Content memberContent = new ContentBuilder();
        writer.addSummaryHeader(this, memberContent);
        return memberContent;
    }

    @Override
    public void addSummary(Content summariesList, Content content) {
        writer.addSummary(HtmlStyle.constantsSummary,
                HtmlIds.ENUM_CONSTANT_SUMMARY, summariesList, content);
    }

    @Override
    public Content getEnumConstantsDetailsHeader(TypeElement typeElement,
                                                 Content memberDetails) {
        memberDetails.add(MarkerComments.START_OF_ENUM_CONSTANT_DETAILS);
        var enumConstantsDetailsContent = new ContentBuilder();
        var heading = HtmlTree.HEADING(Headings.TypeDeclaration.DETAILS_HEADING,
                contents.enumConstantDetailLabel);
        enumConstantsDetailsContent.add(heading);
        return enumConstantsDetailsContent;
    }

    @Override
    public Content getEnumConstantsHeader(VariableElement enumConstant,
                                          Content enumConstantsDetails) {
        Content enumConstantsContent = new ContentBuilder();
        var heading = HtmlTree.HEADING(Headings.TypeDeclaration.MEMBER_HEADING,
                Text.of(name(enumConstant)));
        enumConstantsContent.add(heading);
        return HtmlTree.SECTION(HtmlStyle.detail, enumConstantsContent)
                .setId(htmlIds.forMember(enumConstant));
    }

    @Override
    public Content getSignature(VariableElement enumConstant) {
        return new Signatures.MemberSignature(enumConstant, this)
                .setType(enumConstant.asType())
                .setAnnotations(writer.getAnnotationInfo(enumConstant, true))
                .toContent();
    }

    @Override
    public void addDeprecated(VariableElement enumConstant, Content content) {
        addDeprecatedInfo(enumConstant, content);
    }

    @Override
    public void addPreview(VariableElement enumConstant, Content content) {
        addPreviewInfo(enumConstant, content);
    }

    @Override
    public void addComments(VariableElement enumConstant, Content enumConstants) {
        addComment(enumConstant, enumConstants);
    }

    @Override
    public void addTags(VariableElement enumConstant, Content content) {
        writer.addTagsInfo(enumConstant, content);
    }

    @Override
    public Content getEnumConstantsDetails(Content memberDetailsHeader,
            Content content) {
        return writer.getDetailsListItem(
                HtmlTree.SECTION(HtmlStyle.constantDetails)
                        .setId(HtmlIds.ENUM_CONSTANT_DETAIL)
                        .add(memberDetailsHeader)
                        .add(content));
    }

    @Override
    public void addSummaryLabel(Content content) {
        var label = HtmlTree.HEADING(Headings.TypeDeclaration.SUMMARY_HEADING,
                contents.enumConstantSummary);
        content.add(label);
    }

    @Override
    public TableHeader getSummaryTableHeader(Element member) {
        return new TableHeader(contents.enumConstantLabel, contents.descriptionLabel);
    }

    @Override
    protected Table createSummaryTable() {
        return new Table(HtmlStyle.summaryTable)
                .setCaption(contents.getContent("doclet.Enum_Constants"))
                .setHeader(getSummaryTableHeader(typeElement))
                .setColumnStyles(HtmlStyle.colFirst, HtmlStyle.colLast);
    }

    @Override
    public void addInheritedSummaryLabel(TypeElement typeElement, Content content) {
    }

    @Override
    protected void addSummaryLink(HtmlLinkInfo.Kind context, TypeElement typeElement, Element member,
                                  Content content) {
        Content memberLink = writer.getDocLink(context, utils.getEnclosingTypeElement(member), member,
                name(member), HtmlStyle.memberNameLink);
        var code = HtmlTree.CODE(memberLink);
        content.add(code);
    }

    @Override
    protected void addInheritedSummaryLink(TypeElement typeElement, Element member, Content target) {
    }

    @Override
    protected void addSummaryType(Element member, Content content) {
        //Not applicable.
    }

    @Override
    protected Content getSummaryLink(Element member) {
        String name = utils.getFullyQualifiedName(member) + "." + member.getSimpleName();
        return writer.getDocLink(HtmlLinkInfo.Kind.MEMBER_DEPRECATED_PREVIEW, member, name);
    }

    @Override
    public Content getMemberHeader(){
        return writer.getMemberHeader();
    }
}
