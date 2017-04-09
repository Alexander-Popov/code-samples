<?php
declare(strict_types = 1);

namespace app\Core\Application\Command;

final class ValidationCommandBus implements CommandBusInterface
{
    /**
     * @var CommandBusInterface
     */
    private $next;

    /**
     * @param CommandBusInterface $next
     */
    public function __construct(CommandBusInterface $next) {
        $this->next = $next;
    }

    /**
     * @param $command
     */
    public function execute($command) {
        $validator = $this->resolveValidator($command);
        $validator->validate($command);
        $this->next->execute($command);
    }

    /**
     * @param $command
     * @return object
     * @throws \yii\base\InvalidConfigException
     */
    private function resolveValidator($command) {
        $className = substr(get_class($command), 0, -7) . 'Validator';
        return \Yii::createObject($className);
    }
}