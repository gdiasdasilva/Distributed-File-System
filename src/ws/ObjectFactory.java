
package ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PasteFile_QNAME = new QName("http://trab1/", "pasteFile");
    private final static QName _Dir_QNAME = new QName("http://trab1/", "dir");
    private final static QName _PasteFileResponse_QNAME = new QName("http://trab1/", "pasteFileResponse");
    private final static QName _GetAttr_QNAME = new QName("http://trab1/", "getAttr");
    private final static QName _RmResponse_QNAME = new QName("http://trab1/", "rmResponse");
    private final static QName _GetAttrResponse_QNAME = new QName("http://trab1/", "getAttrResponse");
    private final static QName _DirResponse_QNAME = new QName("http://trab1/", "dirResponse");
    private final static QName _ActiveTestResponse_QNAME = new QName("http://trab1/", "activeTestResponse");
    private final static QName _ActiveTest_QNAME = new QName("http://trab1/", "activeTest");
    private final static QName _Rm_QNAME = new QName("http://trab1/", "rm");
    private final static QName _IOException_QNAME = new QName("http://trab1/", "IOException");
    private final static QName _RmdirResponse_QNAME = new QName("http://trab1/", "rmdirResponse");
    private final static QName _CopyFileResponse_QNAME = new QName("http://trab1/", "copyFileResponse");
    private final static QName _InfoNotFoundException_QNAME = new QName("http://trab1/", "InfoNotFoundException");
    private final static QName _MkdirResponse_QNAME = new QName("http://trab1/", "mkdirResponse");
    private final static QName _CopyFile_QNAME = new QName("http://trab1/", "copyFile");
    private final static QName _Rmdir_QNAME = new QName("http://trab1/", "rmdir");
    private final static QName _Mkdir_QNAME = new QName("http://trab1/", "mkdir");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InfoNotFoundException }
     * 
     */
    public InfoNotFoundException createInfoNotFoundException() {
        return new InfoNotFoundException();
    }

    /**
     * Create an instance of {@link Mkdir }
     * 
     */
    public Mkdir createMkdir() {
        return new Mkdir();
    }

    /**
     * Create an instance of {@link FileInfo }
     * 
     */
    public FileInfo createFileInfo() {
        return new FileInfo();
    }

    /**
     * Create an instance of {@link Rmdir }
     * 
     */
    public Rmdir createRmdir() {
        return new Rmdir();
    }

    /**
     * Create an instance of {@link MkdirResponse }
     * 
     */
    public MkdirResponse createMkdirResponse() {
        return new MkdirResponse();
    }

    /**
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link RmdirResponse }
     * 
     */
    public RmdirResponse createRmdirResponse() {
        return new RmdirResponse();
    }

    /**
     * Create an instance of {@link CopyFileResponse }
     * 
     */
    public CopyFileResponse createCopyFileResponse() {
        return new CopyFileResponse();
    }

    /**
     * Create an instance of {@link PasteFile }
     * 
     */
    public PasteFile createPasteFile() {
        return new PasteFile();
    }

    /**
     * Create an instance of {@link RmResponse }
     * 
     */
    public RmResponse createRmResponse() {
        return new RmResponse();
    }

    /**
     * Create an instance of {@link CopyFile }
     * 
     */
    public CopyFile createCopyFile() {
        return new CopyFile();
    }

    /**
     * Create an instance of {@link DirResponse }
     * 
     */
    public DirResponse createDirResponse() {
        return new DirResponse();
    }

    /**
     * Create an instance of {@link ActiveTestResponse }
     * 
     */
    public ActiveTestResponse createActiveTestResponse() {
        return new ActiveTestResponse();
    }

    /**
     * Create an instance of {@link ActiveTest }
     * 
     */
    public ActiveTest createActiveTest() {
        return new ActiveTest();
    }

    /**
     * Create an instance of {@link PasteFileResponse }
     * 
     */
    public PasteFileResponse createPasteFileResponse() {
        return new PasteFileResponse();
    }

    /**
     * Create an instance of {@link Rm }
     * 
     */
    public Rm createRm() {
        return new Rm();
    }

    /**
     * Create an instance of {@link Dir }
     * 
     */
    public Dir createDir() {
        return new Dir();
    }

    /**
     * Create an instance of {@link GetAttr }
     * 
     */
    public GetAttr createGetAttr() {
        return new GetAttr();
    }

    /**
     * Create an instance of {@link GetAttrResponse }
     * 
     */
    public GetAttrResponse createGetAttrResponse() {
        return new GetAttrResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PasteFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "pasteFile")
    public JAXBElement<PasteFile> createPasteFile(PasteFile value) {
        return new JAXBElement<PasteFile>(_PasteFile_QNAME, PasteFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Dir }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "dir")
    public JAXBElement<Dir> createDir(Dir value) {
        return new JAXBElement<Dir>(_Dir_QNAME, Dir.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PasteFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "pasteFileResponse")
    public JAXBElement<PasteFileResponse> createPasteFileResponse(PasteFileResponse value) {
        return new JAXBElement<PasteFileResponse>(_PasteFileResponse_QNAME, PasteFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAttr }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "getAttr")
    public JAXBElement<GetAttr> createGetAttr(GetAttr value) {
        return new JAXBElement<GetAttr>(_GetAttr_QNAME, GetAttr.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RmResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "rmResponse")
    public JAXBElement<RmResponse> createRmResponse(RmResponse value) {
        return new JAXBElement<RmResponse>(_RmResponse_QNAME, RmResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAttrResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "getAttrResponse")
    public JAXBElement<GetAttrResponse> createGetAttrResponse(GetAttrResponse value) {
        return new JAXBElement<GetAttrResponse>(_GetAttrResponse_QNAME, GetAttrResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DirResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "dirResponse")
    public JAXBElement<DirResponse> createDirResponse(DirResponse value) {
        return new JAXBElement<DirResponse>(_DirResponse_QNAME, DirResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActiveTestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "activeTestResponse")
    public JAXBElement<ActiveTestResponse> createActiveTestResponse(ActiveTestResponse value) {
        return new JAXBElement<ActiveTestResponse>(_ActiveTestResponse_QNAME, ActiveTestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActiveTest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "activeTest")
    public JAXBElement<ActiveTest> createActiveTest(ActiveTest value) {
        return new JAXBElement<ActiveTest>(_ActiveTest_QNAME, ActiveTest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rm }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "rm")
    public JAXBElement<Rm> createRm(Rm value) {
        return new JAXBElement<Rm>(_Rm_QNAME, Rm.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RmdirResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "rmdirResponse")
    public JAXBElement<RmdirResponse> createRmdirResponse(RmdirResponse value) {
        return new JAXBElement<RmdirResponse>(_RmdirResponse_QNAME, RmdirResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CopyFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "copyFileResponse")
    public JAXBElement<CopyFileResponse> createCopyFileResponse(CopyFileResponse value) {
        return new JAXBElement<CopyFileResponse>(_CopyFileResponse_QNAME, CopyFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InfoNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "InfoNotFoundException")
    public JAXBElement<InfoNotFoundException> createInfoNotFoundException(InfoNotFoundException value) {
        return new JAXBElement<InfoNotFoundException>(_InfoNotFoundException_QNAME, InfoNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MkdirResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "mkdirResponse")
    public JAXBElement<MkdirResponse> createMkdirResponse(MkdirResponse value) {
        return new JAXBElement<MkdirResponse>(_MkdirResponse_QNAME, MkdirResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CopyFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "copyFile")
    public JAXBElement<CopyFile> createCopyFile(CopyFile value) {
        return new JAXBElement<CopyFile>(_CopyFile_QNAME, CopyFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rmdir }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "rmdir")
    public JAXBElement<Rmdir> createRmdir(Rmdir value) {
        return new JAXBElement<Rmdir>(_Rmdir_QNAME, Rmdir.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Mkdir }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trab1/", name = "mkdir")
    public JAXBElement<Mkdir> createMkdir(Mkdir value) {
        return new JAXBElement<Mkdir>(_Mkdir_QNAME, Mkdir.class, null, value);
    }

}
