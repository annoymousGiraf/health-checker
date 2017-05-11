class Hello:
    __gui = None

    def __init__(self, gui):
        self.__gui = gui

    def execute(self):
        print 'Hello world!'
        return 'Hello world from Python'

    def getDescription(self):
        return 'Hello world from Python description'