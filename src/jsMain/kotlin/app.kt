import components.download.reader
import components.download.teacherChoose
import components.edit.CEditOpt
import components.edit.container
import components.groups.CGroupContainer
import components.unload.CUnity
import react.FC
import react.Props
import react.create
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import tanstack.query.core.QueryClient
import tanstack.react.query.QueryClientProvider
import tanstack.react.query.devtools.ReactQueryDevtools

val app = FC<Props>("App") {
    HashRouter {
        QueryClientProvider {
            client = QueryClient()
            ReactQueryDevtools { }
            Routes {
                Route{
                    path = "/editOpt/:teacher"
                    element = container.create{}
                }
                Route{
                    path = "/editOpt"
                    element = CEditOpt.create{}
                }
                Route{
                    path = "/unity"
                    element = CUnity.create{}
                }
                Route{
                    path = "/group"
                    element = CGroupContainer.create{}
                }
                Route {
                    path = "/home/:file"
                    element = teacherChoose.create {}
                }
                Route {
                    path = "/home"
                    element = reader.create()
                }
                Route{
                    path = "/"
                    element = CHome.create{}
                }
            }
        }
    }
}
